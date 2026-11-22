<x-admin.layout title="Report Map" :breadcrumbs="['Reports','Map']">
    <div id="report-map" class="h-[70vh] min-h-[32rem] overflow-hidden rounded-2xl bg-slate-100 shadow-sm ring-1 ring-navy/10"></div>
    <script id="report-map-data" type="application/json">@json($reports)</script>
</x-admin.layout>
