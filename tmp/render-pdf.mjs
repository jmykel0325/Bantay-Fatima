import fs from "node:fs";
import path from "node:path";
import { createCanvas, DOMMatrix, ImageData, Path2D } from "./pdf-tools/node_modules/@napi-rs/canvas/index.js";

globalThis.DOMMatrix = DOMMatrix;
globalThis.ImageData = ImageData;
globalThis.Path2D = Path2D;

const { getDocument } = await import("./pdf-tools/node_modules/pdfjs-dist/legacy/build/pdf.mjs");
const input = process.argv[2];
const outputDirectory = process.argv[3];
fs.mkdirSync(outputDirectory, { recursive: true });

const data = new Uint8Array(fs.readFileSync(input));
const document = await getDocument({ data, disableWorker: true }).promise;

for (let pageNumber = 1; pageNumber <= document.numPages; pageNumber += 1) {
  const page = await document.getPage(pageNumber);
  const viewport = page.getViewport({ scale: 2 });
  const canvas = createCanvas(Math.ceil(viewport.width), Math.ceil(viewport.height));
  const context = canvas.getContext("2d");
  await page.render({ canvasContext: context, viewport }).promise;
  fs.writeFileSync(path.join(outputDirectory, `page-${pageNumber}.png`), canvas.toBuffer("image/png"));
}

console.log(`Rendered ${document.numPages} pages to ${outputDirectory}`);
