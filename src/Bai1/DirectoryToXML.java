package Bai1;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DirectoryToXML {

    public static void main(String[] args) {
        String directoryPath = "D:/code C++/Assignment4_C++"; // Thay đổi đường dẫn thư mục ở đây
        String xmlOutputPath = "directoryStructure.xml"; // Đường dẫn file XML đầu ra

        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            Element rootElement = doc.createElement("root");
            doc.appendChild(rootElement);

            Path startPath = Paths.get(directoryPath);
            Files.walkFileTree(startPath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Element fileElement = doc.createElement("file");
                    fileElement.appendChild(doc.createTextNode(sanitizeXMLString(file.getFileName().toString())));
                    appendToParent(doc, file.getParent(), fileElement, rootElement, startPath);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    Element dirElement = doc.createElement(sanitizeXMLString(dir.getFileName().toString()));
                    appendToParent(doc, dir.getParent(), dirElement, rootElement, startPath);
                    return FileVisitResult.CONTINUE;
                }

                private void appendToParent(Document doc, Path parentPath, Element element, Element rootElement, Path startPath) {
                    if (parentPath == null || parentPath.equals(startPath.getParent())) {
                        rootElement.appendChild(element);
                    } else {
                        Element parentElement = findElementByName(rootElement, sanitizeXMLString(parentPath.getFileName().toString()));
                        if (parentElement == null) {
                            parentElement = doc.createElement(sanitizeXMLString(parentPath.getFileName().toString()));
                            appendToParent(doc, parentPath.getParent(), parentElement, rootElement, startPath);
                        }
                        parentElement.appendChild(element);
                    }
                }

                private Element findElementByName(Element root, String name) {
                    for (int i = 0; i < root.getChildNodes().getLength(); i++) {
                        if (root.getChildNodes().item(i).getNodeName().equals(name)) {
                            return (Element) root.getChildNodes().item(i);
                        }
                    }
                    return null;
                }

                private String sanitizeXMLString(String input) {
                    return input.replaceAll("[^a-zA-Z0-9\\-_]", "_");
                }
            });

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(xmlOutputPath));

            transformer.transform(source, result);

            System.out.println("File saved to " + xmlOutputPath);

        } catch (ParserConfigurationException | TransformerException | IOException e) {
            e.printStackTrace();
        }
    }
}