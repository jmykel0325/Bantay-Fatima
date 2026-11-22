import * as maplibregl from 'maplibre-gl';
import 'maplibre-gl/dist/maplibre-gl.css';

// Mapcn is built on MapLibre GL. This focused integration keeps its composable
// marker/popup/control approach while serving the report data rendered by Laravel.
const STATUS_COLORS = {
    pending: '#f59e0b',
    in_progress: '#2563eb',
    resolved: '#16a34a',
    rejected: '#dc2626',
};

// Mapcn accepts custom MapLibre styles. This local, OSM-backed light style is
// used because the remote CARTO style's tiles are unavailable in this browser.
const MAPCN_DARK_STYLE = {
    version: 8,
    sources: {
        osm: {
            type: 'raster',
            tiles: ['https://tile.openstreetmap.org/{z}/{x}/{y}.png'],
            tileSize: 256,
            attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap contributors</a>',
        },
    },
    layers: [{ id: 'osm', type: 'raster', source: 'osm' }],
};

function readableStatus(status) {
    return String(status || 'pending').replaceAll('_', ' ').replace(/\b\w/g, (letter) => letter.toUpperCase());
}

function reportMarker(report) {
    const color = STATUS_COLORS[report.status] || '#0f766e';
    const element = document.createElement('button');
    element.type = 'button';
    element.className = 'report-map-marker';
    element.setAttribute('aria-label', `View ${report.reference_number || 'report'} on map`);
    element.style.setProperty('--marker-color', color);
    element.innerHTML = '<span></span>';
    return element;
}

function reportPopup(report) {
    const content = document.createElement('article');
    content.className = 'min-w-56 p-1 text-slate-700';

    const reference = document.createElement('p');
    reference.className = 'text-xs font-bold tracking-wide text-teal-700';
    reference.textContent = report.reference_number || 'Community report';

    const title = document.createElement('h2');
    title.className = 'mt-1 text-sm font-bold text-slate-900';
    title.textContent = report.title || report.category?.name || 'Untitled report';

    const status = document.createElement('p');
    status.className = 'mt-2 inline-flex rounded-full px-2 py-1 text-xs font-semibold';
    status.style.backgroundColor = `${STATUS_COLORS[report.status] || '#0f766e'}1a`;
    status.style.color = STATUS_COLORS[report.status] || '#0f766e';
    status.textContent = readableStatus(report.status);

    const location = document.createElement('p');
    location.className = 'mt-2 text-xs text-slate-500';
    location.textContent = [report.purok?.name, report.address].filter(Boolean).join(' · ') || 'Location tagged by resident';

    content.append(reference, title, status, location);
    return content;
}

function addMapcnStyles() {
    if (document.getElementById('report-mapcn-styles')) return;
    const styles = document.createElement('style');
    styles.id = 'report-mapcn-styles';
    styles.textContent = `
        .report-map-marker { width: 30px; height: 30px; border: 3px solid white; border-radius: 9999px 9999px 9999px 0; background: var(--marker-color); box-shadow: 0 4px 10px rgb(15 23 42 / .35); cursor: pointer; transform: rotate(-45deg); }
        .report-map-marker span { display: block; width: 9px; height: 9px; margin: 7px; border-radius: 9999px; background: white; }
        .maplibregl-popup-content { border-radius: 12px; box-shadow: 0 12px 28px rgb(15 23 42 / .18); }
        .maplibregl-ctrl-group { overflow: hidden; border-radius: 10px; box-shadow: 0 4px 14px rgb(15 23 42 / .16); }
        .report-map-summary { position: absolute; z-index: 2; top: 1rem; right: 1rem; width: min(15rem, calc(100% - 6rem)); border: 1px solid rgb(148 163 184 / .38); border-radius: .9rem; background: rgb(255 255 255 / .94); padding: 1rem; color: #0f172a; box-shadow: 0 14px 32px rgb(15 23 42 / .16); backdrop-filter: blur(12px); }
        .report-map-summary h2 { margin: 0; font-size: .875rem; font-weight: 700; }
        .report-map-summary p { margin: .25rem 0 0; font-size: .75rem; color: #64748b; }
        .report-map-summary ul { display: grid; grid-template-columns: repeat(2, minmax(0, 1fr)); gap: .5rem; margin: .85rem 0 0; padding: 0; list-style: none; }
        .report-map-summary li { font-size: .72rem; color: #64748b; }
        .report-map-summary strong { display: block; font-size: 1rem; color: #0f172a; }
    `;
    document.head.append(styles);
}

function addReportSummary(container, reports) {
    const counts = reports.reduce((result, report) => {
        const status = report.status || 'pending';
        result[status] = (result[status] || 0) + 1;
        return result;
    }, {});
    const summary = document.createElement('aside');
    summary.className = 'report-map-summary';
    summary.setAttribute('aria-label', 'Report map summary');
    summary.innerHTML = `<h2>Community reports</h2><p>${reports.length} location${reports.length === 1 ? '' : 's'} shown</p><ul>${Object.entries(counts).map(([status, count]) => `<li><strong style="color:${STATUS_COLORS[status] || '#2dd4bf'}">${count}</strong>${readableStatus(status)}</li>`).join('')}</ul>`;
    container.append(summary);
}

function mountReportMap() {
    const container = document.getElementById('report-map');
    const source = document.getElementById('report-map-data');
    if (!container || !source || container.dataset.mounted) return;
    container.dataset.mounted = 'true';

    let reports = [];
    try {
        reports = JSON.parse(source.textContent || '[]');
    } catch {
        container.textContent = 'Unable to load report locations.';
        return;
    }

    addMapcnStyles();
    const map = new maplibregl.Map({
        container,
        style: MAPCN_DARK_STYLE,
        center: [125.1716, 6.1164],
        zoom: 14,
        attributionControl: true,
    });

    map.addControl(new maplibregl.NavigationControl(), 'top-left');
    map.addControl(new maplibregl.FullscreenControl(), 'top-left');
    addReportSummary(container, reports);

    const bounds = new maplibregl.LngLatBounds();
    reports.forEach((report) => {
        const longitude = Number(report.longitude);
        const latitude = Number(report.latitude);
        if (!Number.isFinite(longitude) || !Number.isFinite(latitude)) return;

        const popup = new maplibregl.Popup({ offset: 22, closeButton: true }).setDOMContent(reportPopup(report));
        new maplibregl.Marker({ element: reportMarker(report), anchor: 'bottom' })
            .setLngLat([longitude, latitude])
            .setPopup(popup)
            .addTo(map);
        bounds.extend([longitude, latitude]);
    });

    map.once('load', () => {
        if (!bounds.isEmpty()) map.fitBounds(bounds, { padding: 56, maxZoom: 16, duration: 0 });
    });
}

if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', mountReportMap, { once: true });
} else {
    mountReportMap();
}
