package Bai2;

import java.util.ArrayList;
import java.util.List;

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

public class StudentList {
    public static void main(String[] args) {
        List<Student> students = new ArrayList<>();
        students.add(new Student("Nguyen Van A", 20, 3.5));
        students.add(new Student("Le Thi B", 21, 3.7));
        students.add(new Student("Tran Van C", 22, 3.9));

        // Xuất danh sách ra file XML
        exportToXML(students, "students.xml");
    }

    private static void exportToXML(List<Student> students, String filePath) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // Root element
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("students");
            doc.appendChild(rootElement);

            for (Student student : students) {
                // Student element
                Element studentElement = doc.createElement("student");

                // Name element
                Element name = doc.createElement("name");
                name.appendChild(doc.createTextNode(student.getName()));
                studentElement.appendChild(name);

                // Age element
                Element age = doc.createElement("age");
                age.appendChild(doc.createTextNode(String.valueOf(student.getAge())));
                studentElement.appendChild(age);

                // GPA element
                Element gpa = doc.createElement("gpa");
                gpa.appendChild(doc.createTextNode(String.valueOf(student.getGpa())));
                studentElement.appendChild(gpa);

                rootElement.appendChild(studentElement);
            }

            // Write the content into XML file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File());

            transformer.transform(source, result);

            System.out.println("File saved to " + filePath);

        } catch (ParserConfigurationException | TransformerException pce) {
            pce.printStackTrace();
        }
    }
}
