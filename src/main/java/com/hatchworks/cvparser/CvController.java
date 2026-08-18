//-------------------------------------------------------------------------------
//CREADOR: DIANA MORALES FUENTES CEDULA 40550101
//-------------------------------------------------------------------------------
//Esta clase va a recibir las visitas de la pagina web 
//-------------------------------------------------------------------------------
package com.hatchworks.cvparser;
//-------------------------------------------------------------------------------
//LIBERIAS NECESARIAS
//-------------------------------------------------------------------------------
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
//-------------------------------------------------------------------------------
//LIBRERIAS PARA LEER LO QUE HAY DENTRO DE LOS PDF'S
//-------------------------------------------------------------------------------
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.ui.Model;
//-------------------------------------------------------------------------------
//LIBRERIAS DESCARGAR PDF
//-------------------------------------------------------------------------------
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import java.io.ByteArrayOutputStream;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
//-------------------------------------------------------------------------------
//LIBRERIA AGREGAR DATOS DEL CV AL PDF DESCARGADO
//-------------------------------------------------------------------------------
import org.springframework.web.bind.annotation.RequestParam;
//-------------------------------------------------------------------------------

@Controller
public class CvController {
//-------------------------------------------------------------------------------
//LECTURA DEL CV SUBIDO
//-------------------------------------------------------------------------------
    @GetMapping("/")
    public String inicio() {
        return "index";
    }
//-------------------------------------------------------------------------------
//VALIDACION SI NO SE SUBE NINGUN ARCHIVO 
//-------------------------------------------------------------------------------   
    @PostMapping("/upload")
    public String uploadCv(MultipartFile file, Model model) {
        
        if (file.isEmpty()) {
    model.addAttribute("error", "Please select a PDF file.");//la idea es que aca muestre error si no hay datos 
    return "index";
}
 //-------------------------------------------------------------------------------
 //VALIDACION SI SE SUBE UN ARCHIVO QUE NO ES PDF esto es una segunda validacion porque el index ya lo hace
 //-------------------------------------------------------------------------------
        if (!file.getOriginalFilename().toLowerCase().endsWith(".pdf")) {
    model.addAttribute("error", "Unsupported file format. Please upload a PDF.");
    return "index";
}
//-------------------------------------------------------------------------------
//SI SI HAY ARCHIVO PDF
//-------------------------------------------------------------------------------
    System.out.println("Archivo recibido: " + file.getOriginalFilename());// si si hay archivo debe obtenerlo 
    
//-------------------------------------------------------------------------------
//IMPRESION DE LOS DATOS ENCONTRADOS 
//-------------------------------------------------------------------------------
    try {

        PDDocument document = Loader.loadPDF(file.getBytes());

        PDFTextStripper stripper = new PDFTextStripper();

        String texto = stripper.getText(document);

        CvParser parser = new CvParser();

        CvData datos = parser.parse(texto);
        model.addAttribute("cv", datos);
        
        System.out.println("Nombre encontrado: " + datos.getName());
        System.out.println("Email encontrado: " + datos.getEmail());
        System.out.println("Teléfono encontrado: " + datos.getPhone());
        
        System.out.println("Educación encontrada:");
        System.out.println(datos.getEducation());
        
        System.out.println("Skills encontradas:");
        System.out.println(datos.getSkills());
        
        System.out.println("Experiencia encontrada:");
        System.out.println(datos.getExperience());

        document.close();

        System.out.println("===== TEXTO DEL CV =====");
        System.out.println(texto);
        System.out.println("========================");
//-------------------------------------------------------------------------------
//EXCEPCIONES
//-------------------------------------------------------------------------------

    } catch (Exception e) {

        System.out.println("No se pudo leer el PDF.");
        System.out.println(e.getMessage());
            
        model.addAttribute("error", "We could not read this PDF. Please try another file.");
        return "index";

    }
    
    return "result";
}
    //---------------------------------------------------------------------------------------
    //METODO DESCARGAR PDF
    //---------------------------------------------------------------------------------------
    @PostMapping("/download")
public ResponseEntity<byte[]> downloadPdf(
        @RequestParam String name,
        @RequestParam String email,
        @RequestParam String phone,
        @RequestParam String education,
        @RequestParam String skills,
        @RequestParam String experience) {

    try {

        String html =
                "<html>" +

                "<head>" +

                "<meta charset=\"UTF-8\" />" +

                "<style>" +

                "body {" +
                "font-family: Arial, sans-serif;" +
                "margin: 35px;" +
                "color: #1f2937;" +
                "font-size: 12px;" +
                "}" +

                ".header {" +
                "background-color: #1f2937;" +
                "color: white;" +
                "padding: 28px;" +
                "margin-bottom: 28px;" +
                "}" +

                ".header-label {" +
                "font-size: 10px;" +
                "letter-spacing: 2px;" +
                "margin-bottom: 10px;" +
                "}" +

                ".header-name {" +
                "font-size: 26px;" +
                "font-weight: bold;" +
                "margin-bottom: 12px;" +
                "}" +

                ".contact {" +
                "font-size: 11px;" +
                "}" +

                ".section {" +
                "margin-bottom: 25px;" +
                "}" +

                ".section-title {" +
                "font-size: 16px;" +
                "font-weight: bold;" +
                "color: #1f2937;" +
                "border-bottom: 1px solid #d1d5db;" +
                "padding-bottom: 6px;" +
                "margin-bottom: 12px;" +
                "}" +

                ".section-content {" +
                "font-size: 11px;" +
                "line-height: 1.6;" +
                "white-space: pre-line;" +
                "}" +

                "</style>" +

                "</head>" +

                "<body>" +

                // ENCABEZADO
                "<div class=\"header\">" +

                "<div class=\"header-label\">" +
                "PROFESSIONAL PROFILE" +
                "</div>" +

                "<div class=\"header-name\">" +
                limpiarHtml(name) +
                "</div>" +

                "<div class=\"contact\">" +
                limpiarHtml(email) +
                "   |   " +
              
                limpiarHtml(phone) +
                "</div>" +

                "</div>" +

                // EDUCATION
                "<div class=\"section\">" +

                "<div class=\"section-title\">" +
                "EDUCATION" +
                "</div>" +

                "<div class=\"section-content\">" +
                limpiarHtml(education) +
                "</div>" +

                "</div>" +

                // SKILLS
                "<div class=\"section\">" +

                "<div class=\"section-title\">" +
                "SKILLS" +
                "</div>" +

                "<div class=\"section-content\">" +
                limpiarHtml(skills) +
                "</div>" +

                "</div>" +

                // EXPERIENCE
                "<div class=\"section\">" +

                "<div class=\"section-title\">" +
                "EXPERIENCE" +
                "</div>" +

                "<div class=\"section-content\">" +
                limpiarHtml(experience) +
                "</div>" +

                "</div>" +

                "</body>" +

                "</html>";

        ByteArrayOutputStream outputStream =
                new ByteArrayOutputStream();

        PdfRendererBuilder builder =
                new PdfRendererBuilder();

        builder.withHtmlContent(html, null);

        builder.toStream(outputStream);

        builder.run();

        byte[] pdfBytes =
                outputStream.toByteArray();

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=profile.pdf"
                )
                .header(
                        HttpHeaders.CONTENT_TYPE,
                        "application/pdf"
                )
                .body(pdfBytes);

    } catch (Exception e) {

        System.out.println("Error generando PDF:");
        e.printStackTrace();

        return ResponseEntity
                .internalServerError()
                .build();
    }
}
//---------------------------------------------------------------------------------------
//LEER CARACTERES Y SIMBOLOS DEL CV
//---------------------------------------------------------------------------------------
private String limpiarHtml(String texto) {

    if (texto == null) {
        return "";
    }

    return texto
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;");
  //---------------------------------------------------------------------------------------
}




}