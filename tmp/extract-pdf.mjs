import fs from "node:fs";
import { getDocument } from "./pdf-tools/node_modules/pdfjs-dist/legacy/build/pdf.mjs";

const input = process.argv[2];
const data = new Uint8Array(fs.readFileSync(input));
const document = await getDocument({ data, disableWorker: true }).promise;

console.log(`PAGES=${document.numPages}`);
for (let pageNumber = 1; pageNumber <= document.numPages; pageNumber += 1) {
  const page = await document.getPage(pageNumber);
  const content = await page.getTextContent();
  const lines = [];
  let current = "";
  let previousY = null;

  for (const item of content.items) {
    if (!("str" in item)) continue;
    const y = Math.round(item.transform[5]);
    if (previousY !== null && Math.abs(y - previousY) > 2) {
      if (current.trim()) lines.push(current.trim());
      current = "";
    }
    current += `${item.str} `;
    previousY = y;
  }
  if (current.trim()) lines.push(current.trim());

  console.log(`\n--- PAGE ${pageNumber} ---`);
  console.log(lines.join("\n"));
}
