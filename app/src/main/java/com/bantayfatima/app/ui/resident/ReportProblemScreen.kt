@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.bantayfatima.app.ui.resident

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bantayfatima.app.data.model.PurokDto
import com.bantayfatima.app.data.model.ReportCategoryDto
import com.bantayfatima.app.ui.components.*
import com.bantayfatima.app.ui.theme.*
import java.io.File

@Composable
fun ReportProblemScreen(
    onBack: () -> Unit,
    onHome: () -> Unit,
    onMyReports: () -> Unit,
    viewModel: ReportViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val submitted = state.submitted
    if (submitted != null) {
        ReportSuccessScreen(submitted.referenceNumber.orEmpty(), onMyReports) { viewModel.reset(); onHome() }
        return
    }
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            ScreenTopBar(
                title = when (state.step) { 2 -> "Location & Evidence"; 3 -> "Review Your Report"; else -> "Report a Problem" },
                subtitle = when (state.step) { 2 -> "Add photos and confirm the location"; 3 -> "Confirm the information before submitting"; else -> "Provide clear information about the concern" },
                onBack = { if (state.step > 1) viewModel.goTo(state.step - 1) else onBack() },
            )
        },
    ) { padding ->
        LazyColumn(
            Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(horizontal = Spacing.screen, vertical = Spacing.sm),
            verticalArrangement = Arrangement.spacedBy(Spacing.md),
        ) {
            item { ReportProgress(state.step) }
            state.error?.let { message -> item { InfoCard(message, Icons.Filled.Info, accent = if (message.startsWith("Draft")) BantayPrimary else MaterialTheme.colorScheme.error) } }
            when (state.step) {
                1 -> item { ReportDetailsStep(state, viewModel) }
                2 -> item { ReportLocationEvidenceStep(state, viewModel) }
                else -> item { ReportReviewStep(state, viewModel) }
            }
        }
    }
}

@Composable private fun ReportProgress(step: Int) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        LinearProgressIndicator(progress = { step / 3f }, modifier = Modifier.fillMaxWidth())
        Text("1 Details   →   2 Location & Evidence   →   3 Review", style = MaterialTheme.typography.labelSmall, color = BantayMuted)
    }
}

@Composable private fun ReportDetailsStep(state: ReportFormState, vm: ReportViewModel) {
    Column(verticalArrangement = Arrangement.spacedBy(Spacing.md)) {
        OptionDropdown("Category", state.categoryName.ifBlank { "Select category" }, state.categories, { it.name.orEmpty() }, vm::selectCategory)
        if (state.categoryName.equals("Other", true)) AppTextField(state.specifyConcern, { value -> vm.update { it.copy(specifyConcern = value.take(100)) } }, "Specify Concern", leadingIcon = Icons.Filled.Category)
        AppTextField(state.title, { value -> vm.update { it.copy(title = value.take(100)) } }, "Report Title", leadingIcon = Icons.Filled.Description, helperText = "Briefly describe the concern · ${state.title.length} / 100")
        OutlinedTextField(
            value = state.description, onValueChange = { value -> vm.update { it.copy(description = value.take(500)) } },
            label = { Text("Description") }, placeholder = { Text("Describe what happened, where it is located, and details that may help the barangay respond.") },
            minLines = 5, maxLines = 8, shape = FieldShape, modifier = Modifier.fillMaxWidth(),
            supportingText = { Text("${state.description.length} / 500") },
        )
        OptionDropdown("Purok", state.purokName.ifBlank { "Select purok" }, state.puroks, { it.name.orEmpty() }, vm::selectPurok)
        AppTextField(state.landmark, { value -> vm.update { it.copy(landmark = value.take(500)) } }, "Optional Landmark", leadingIcon = Icons.Filled.LocationCity, helperText = "Example: Near Fatima Elementary School")
        Row(horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
            SecondaryButton("Save as Draft", Modifier.weight(1f), onClick = vm::saveDraft)
            PrimaryButton("Continue", Modifier.weight(1f), onClick = vm::continueFromDetails)
        }
    }
}

@Composable private fun <T> OptionDropdown(label: String, selected: String, options: List<T>, text: (T) -> String, onSelect: (T) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded, { expanded = !expanded }) {
        OutlinedTextField(selected, {}, readOnly = true, label = { Text(label) }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) }, shape = FieldShape, modifier = Modifier.menuAnchor().fillMaxWidth())
        ExposedDropdownMenu(expanded, { expanded = false }) { options.forEach { item -> DropdownMenuItem({ Text(text(item)) }, { onSelect(item); expanded = false }) } }
    }
}

@Composable private fun ReportLocationEvidenceStep(state: ReportFormState, vm: ReportViewModel) {
    val context = LocalContext.current
    var cameraUri by remember { mutableStateOf<Uri?>(null) }
    val gallery = rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris -> vm.addPhotos(uris.map(Uri::toString)) }
    val camera = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success -> if (success) cameraUri?.let { vm.addPhotos(listOf(it.toString())) } }
    fun openCamera() {
        val dir = File(context.cacheDir, "report_photos").apply { mkdirs() }
        cameraUri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", File(dir, "report-${System.currentTimeMillis()}.jpg"))
        cameraUri?.let(camera::launch)
    }
    val cameraPermission = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { if (it) openCamera() }
    fun useLocation() {
        val manager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val location = listOf(LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER).firstNotNullOfOrNull { provider -> runCatching { manager.getLastKnownLocation(provider) }.getOrNull() }
        if (location != null) vm.setLocation(location.latitude, location.longitude) else vm.update { it.copy(error = "Current location is unavailable. Use Adjust Pin to select the location manually.") }
    }
    val locationPermission = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { grants -> if (grants.values.any { it }) useLocation() else vm.update { it.copy(error = "Location helps barangay personnel find the concern. Permission was denied, but you can still use Adjust Pin.") } }

    Column(verticalArrangement = Arrangement.spacedBy(Spacing.md)) {
        Text("Location", style = MaterialTheme.typography.titleMedium, color = BantayNavy)
        MapPreview(state)
        Text(if (state.latitude == null) "No location selected" else "${state.purokName.ifBlank { "Barangay Fatima" }}${state.landmark.takeIf(String::isNotBlank)?.let { " · Near $it" }.orEmpty()}", style = MaterialTheme.typography.bodyMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
            SecondaryButton("Use Current Location", Modifier.weight(1f), icon = Icons.Filled.MyLocation) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) useLocation()
                else locationPermission.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
            }
            SecondaryButton("Adjust Pin", Modifier.weight(1f), icon = Icons.Filled.EditLocation) { vm.setLocation(state.latitude ?: 6.1164, state.longitude ?: 125.1716) }
        }
        if (state.latitude != null) {
            Row(horizontalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                TextButton({ vm.setLocation(state.latitude + .0001, state.longitude!!) }) { Text("North") }
                TextButton({ vm.setLocation(state.latitude - .0001, state.longitude!!) }) { Text("South") }
                TextButton({ vm.setLocation(state.latitude, state.longitude!! - .0001) }) { Text("West") }
                TextButton({ vm.setLocation(state.latitude, state.longitude!! + .0001) }) { Text("East") }
            }
        }
        Text("Supporting Photos (${state.photos.size}/5)", style = MaterialTheme.typography.titleMedium, color = BantayNavy)
        Text("Photos are optional but can help barangay personnel understand the concern.", style = MaterialTheme.typography.bodySmall, color = BantayMuted)
        Row(horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
            SecondaryButton("Camera", Modifier.weight(1f), icon = Icons.Filled.PhotoCamera) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) openCamera() else cameraPermission.launch(Manifest.permission.CAMERA)
            }
            SecondaryButton("Gallery", Modifier.weight(1f), icon = Icons.Filled.PhotoLibrary) { gallery.launch("image/*") }
        }
        PhotoStrip(state.photos, vm::removePhoto)
        InfoCard("Please avoid uploading photos containing unnecessary sensitive personal information.", Icons.Filled.PrivacyTip)
        Row(horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
            SecondaryButton("Back", Modifier.weight(1f)) { vm.goTo(1) }
            PrimaryButton("Continue to Review", Modifier.weight(1f), onClick = vm::continueFromLocation)
        }
    }
}

@Composable private fun MapPreview(state: ReportFormState) {
    Box(Modifier.fillMaxWidth().height(170.dp).clip(CardShape).background(BantayPrimarySoft), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Filled.LocationOn, null, tint = BantayPrimary, modifier = Modifier.size(42.dp))
            Text(if (state.latitude == null) "Select the concern location" else "Location pin selected", fontWeight = FontWeight.SemiBold)
            if (state.latitude != null) Text("${"%.5f".format(state.latitude)}, ${"%.5f".format(state.longitude)}", style = MaterialTheme.typography.labelSmall, color = BantayMuted)
        }
    }
}

@Composable private fun PhotoStrip(photos: List<String>, onRemove: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(Spacing.xs)) { photos.forEach { value ->
        val context = LocalContext.current
        val bitmap = remember(value) { runCatching { context.contentResolver.openInputStream(Uri.parse(value))?.use(android.graphics.BitmapFactory::decodeStream) }.getOrNull() }
        AppCard(Modifier.fillMaxWidth()) { Row(Modifier.padding(Spacing.xs), verticalAlignment = Alignment.CenterVertically) {
            if (bitmap != null) Image(bitmap.asImageBitmap(), null, Modifier.size(58.dp).clip(RoundedCornerShape(8.dp)), contentScale = ContentScale.Crop)
            Spacer(Modifier.weight(1f)); IconButton({ onRemove(value) }) { Icon(Icons.Filled.Close, "Remove photo", tint = MaterialTheme.colorScheme.error) }
        } }
    } }
}

@Composable private fun ReportReviewStep(state: ReportFormState, vm: ReportViewModel) {
    Column(verticalArrangement = Arrangement.spacedBy(Spacing.md)) {
        ReviewSection("Concern Details", { vm.goTo(1) }) {
            ReviewLine("Category", if (state.categoryName.equals("Other", true)) "Other · ${state.specifyConcern}" else state.categoryName)
            ReviewLine("Title", state.title); ReviewLine("Description", state.description); ReviewLine("Purok", state.purokName)
            if (state.landmark.isNotBlank()) ReviewLine("Landmark", state.landmark)
        }
        ReviewSection("Location", { vm.goTo(2) }) { MapPreview(state); ReviewLine("Selected location", state.purokName) }
        ReviewSection("Supporting Photos", { vm.goTo(2) }) { if (state.photos.isEmpty()) Text("No supporting photos added.", color = BantayMuted) else PhotoStrip(state.photos, vm::removePhoto) }
        Row(verticalAlignment = Alignment.Top) { Checkbox(state.declaration, { vm.update { s -> s.copy(declaration = it) } }); Text("I confirm that the information I provided is accurate to the best of my knowledge.", Modifier.padding(top = 12.dp)) }
        Text("False, misleading, or malicious reports may be reviewed by Barangay Fatima administrators.", style = MaterialTheme.typography.bodySmall, color = BantayMuted)
        SecondaryButton("Save as Draft", onClick = vm::saveDraft)
        PrimaryButton("Submit Report", loading = state.submitting, enabled = state.declaration && !state.submitting, icon = Icons.Filled.Send, onClick = vm::submit)
    }
}

@Composable private fun ReviewSection(title: String, onEdit: () -> Unit, content: @Composable ColumnScope.() -> Unit) {
    AppCard(Modifier.fillMaxWidth()) { Column(Modifier.padding(Spacing.md), verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
        Row(verticalAlignment = Alignment.CenterVertically) { Text(title, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f)); TextButton(onEdit) { Icon(Icons.Filled.Edit, null); Text("Edit") } }
        content()
    } }
}
@Composable private fun ReviewLine(label: String, value: String) { Column { Text(label, style = MaterialTheme.typography.labelSmall, color = BantayMuted); Text(value, style = MaterialTheme.typography.bodyMedium) } }

@Composable private fun ReportSuccessScreen(reference: String, onView: () -> Unit, onHome: () -> Unit) {
    Scaffold(containerColor = MaterialTheme.colorScheme.background) { padding -> Column(Modifier.fillMaxSize().padding(padding).padding(Spacing.screen), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Icon(Icons.Filled.CheckCircle, null, tint = AppTheme.status.resolved, modifier = Modifier.size(64.dp)); Spacer(Modifier.height(Spacing.md))
        Text("Report Submitted", style = MaterialTheme.typography.headlineSmall, color = BantayNavy); Text("Your concern has been submitted to Barangay Fatima for review.", textAlign = TextAlign.Center, color = BantayMuted)
        Spacer(Modifier.height(Spacing.lg)); AppCard(Modifier.fillMaxWidth()) { Column(Modifier.padding(Spacing.lg), horizontalAlignment = Alignment.CenterHorizontally) { Text("Reference Number", color = BantayMuted); Text(reference, style = MaterialTheme.typography.titleLarge); StatusBadge("Pending Review", StatusTone.Pending); Spacer(Modifier.height(Spacing.sm)); Text("The barangay administrator will review the report before it is assigned for action.", textAlign = TextAlign.Center, style = MaterialTheme.typography.bodySmall) } }
        Spacer(Modifier.height(Spacing.lg)); PrimaryButton("View My Report", onClick = onView); Spacer(Modifier.height(Spacing.sm)); SecondaryButton("Return Home", onClick = onHome)
    } }
}
