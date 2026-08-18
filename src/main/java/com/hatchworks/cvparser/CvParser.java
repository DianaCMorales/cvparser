// CREADOR: DIANA MORALES FUENTES CEDULA 40550101
// -------------------------------------------------------------------------------
package com.hatchworks.cvparser;

// -------------------------------------------------------------------------------
// LIBRERIAS NECESARIAS
// -------------------------------------------------------------------------------
import java.util.regex.Matcher;
import java.util.regex.Pattern;
// -------------------------------------------------------------------------------

public class CvParser {

    public CvData parse(String texto) {

        CvData datos = new CvData();

        // -----------------------------------------------------------------------
        // BUSCAR NOMBRE
        // -----------------------------------------------------------------------
       String[] lineas = texto.split("\\R");

String nombre = "Not detected";

for (int i = 0; i < lineas.length; i++) {

    String linea = lineas[i].trim();

    if (!linea.isEmpty()) {

        nombre = linea;

        // Si la primera línea tiene una sola palabra,
        // revisamos si el apellido está en la siguiente línea.
        if (!linea.contains(" ")) {

            for (int j = i + 1; j < lineas.length; j++) {

                String siguienteLinea = lineas[j].trim();

                if (!siguienteLinea.isEmpty()) {

                    // Evitar tomar encabezados como DETAILS, PHONE, EMAIL, etc.
                    if (!siguienteLinea.equalsIgnoreCase("DETAILS")
                            && !siguienteLinea.equalsIgnoreCase("PHONE")
                            && !siguienteLinea.equalsIgnoreCase("EMAIL")
                            && !siguienteLinea.equalsIgnoreCase("SKILLS")
                            && !siguienteLinea.equalsIgnoreCase("SUMMARY")) {

                        nombre = linea + " " + siguienteLinea;
                    }

                    break;
                }
            }
        }

        break;
    }
}

datos.setName(nombre);

        // -----------------------------------------------------------------------
        // BUSCAR EMAIL
        // -----------------------------------------------------------------------

        Pattern emailPattern = Pattern.compile(
                "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}"
        );

        Matcher emailMatcher = emailPattern.matcher(texto);

        if (emailMatcher.find()) {

            datos.setEmail(emailMatcher.group());

        } else {

            datos.setEmail("Not detected");
        }

        // -----------------------------------------------------------------------
        // BUSCAR TELEFONO
        // -----------------------------------------------------------------------

        Pattern phonePattern = Pattern.compile(
        "(?:\\(\\+\\d{1,3}\\)|\\+\\d{1,3})[\\s.-]*\\d{4}[\\s.-]?\\d{4}"
        + "|\\d{4}[\\s.-]\\d{4}"
         );
        
        
        Matcher phoneMatcher = phonePattern.matcher(texto);

        if (phoneMatcher.find()) {

            datos.setPhone(phoneMatcher.group().trim());

        } else {

            datos.setPhone("Not detected");
        }

        // -----------------------------------------------------------------------
        // POSIBLES TITULOS DE EDUCACION
        // -----------------------------------------------------------------------

        String[] titulosEducacion = {
         "Education",
         "Academic Background",
         "Education and Training",
         "Educación",
         "Educacion",
         "Formación Académica",
         "Formacion Academica"
           };

        // -----------------------------------------------------------------------
        // POSIBLES TITULOS DE SKILLS
        // -----------------------------------------------------------------------

        String[] titulosSkills = {
            "Technical Skills",
            "Skills",
            "Core Skills",
            "Key Skills",
            "Habilidades",
            "Habilidades Técnicas",
            "Habilidades Tecnicas"
        };

        // -----------------------------------------------------------------------
        // POSIBLES TITULOS DE EXPERIENCIA
        // -----------------------------------------------------------------------

        String[] titulosExperiencia = {
            "Professional Experience",
            "Work Experience",
            "Experience",
            "Employment History",
            "Experiencia Profesional",
            "Experiencia Laboral"
        };

        // -----------------------------------------------------------------------
        // POSIBLES TITULOS DE PROYECTOS
        // -----------------------------------------------------------------------

        String[] titulosProyectos = {
            "Academic Project",
            "Academic Projects",
            "Projects",
            "Personal Projects",
            "Proyectos",
            "Proyectos Académicos",
            "University Projects",
            "Certifications",
            "Certificaciones"
        };
        
        String[] titulosResumen = {
        "Summary",
        "Professional Summary",
        "Profile",
        "Professional Profile",
        "Resumen",
        "Perfil Profesional"
        };

        // -----------------------------------------------------------------------
        // EXTRAER EDUCACION
        // Se detiene cuando encuentra Skills o Experience
        // -----------------------------------------------------------------------

        String[] finalesEducacion = unirArreglos(
                titulosSkills,
                titulosExperiencia,
                titulosProyectos
        );

        String education = extraerSeccionFlexible(
                texto,
                titulosEducacion,
                finalesEducacion
        );

        datos.setEducation(education);

        // -----------------------------------------------------------------------
        // EXTRAER SKILLS
        // Se detiene cuando encuentra Experience, Education o Projects
        // -----------------------------------------------------------------------

        String[] finalesSkills = unirCuatroArreglos(
        titulosExperiencia,
        titulosEducacion,
        titulosProyectos,
        titulosResumen
        );

        String skills = extraerSeccionFlexible(
                texto,
                titulosSkills,
                finalesSkills
        );

        datos.setSkills(skills);

        // -----------------------------------------------------------------------
        // EXTRAER EXPERIENCIA
        // Se detiene cuando encuentra Projects, Education o Skills
        // -----------------------------------------------------------------------

        String[] finalesExperiencia = unirArreglos(
                titulosProyectos,
                titulosEducacion,
                titulosSkills
        );

        String experience = extraerSeccionFlexible(
                texto,
                titulosExperiencia,
                finalesExperiencia
        );

        datos.setExperience(experience);

        return datos;
    }

    // ---------------------------------------------------------------------------
    // METODO PARA EXTRAER UNA SECCION UTILIZANDO VARIOS TITULOS POSIBLES
    // ---------------------------------------------------------------------------

    private String extraerSeccionFlexible(
        String texto,
        String[] titulosInicio,
        String[] titulosFin) {

    String[] lineas = texto.split("\\R");

    int lineaInicio = -1;
    String tituloInicioEncontrado = "";

    // Buscar una línea que sea exactamente uno de los títulos de inicio
    for (int i = 0; i < lineas.length; i++) {

        String lineaLimpia = lineas[i].trim();

        for (String titulo : titulosInicio) {

            if (lineaLimpia.equalsIgnoreCase(titulo)) {

                lineaInicio = i;
                tituloInicioEncontrado = titulo;
                break;
            }
        }

        if (lineaInicio != -1) {
            break;
        }
    }

    // Si no encontramos el título
    if (lineaInicio == -1) {
        return "Not detected";
    }

    int lineaFin = lineas.length;

    // Buscar el siguiente título válido después del inicio
    for (int i = lineaInicio + 1; i < lineas.length; i++) {

        String lineaLimpia = lineas[i].trim();

        for (String tituloFin : titulosFin) {

            if (lineaLimpia.equalsIgnoreCase(tituloFin)) {

                lineaFin = i;
                break;
            }
        }

        if (lineaFin != lineas.length) {
            break;
        }
    }

    StringBuilder contenido = new StringBuilder();

    for (int i = lineaInicio + 1; i < lineaFin; i++) {

        if (!lineas[i].trim().isEmpty()) {

            contenido.append(lineas[i].trim());
            contenido.append("\n");
        }
    }

    String resultado = contenido.toString().trim();

    if (resultado.isEmpty()) {
        return "Not detected";
    }

    return resultado;
}
    // ---------------------------------------------------------------------------
    // METODO PARA UNIR VARIAS LISTAS DE TITULOS
    // ---------------------------------------------------------------------------

    private String[] unirArreglos(
            String[] primero,
            String[] segundo,
            String[] tercero) {

        String[] resultado = new String[
                primero.length
                + segundo.length
                + tercero.length
        ];

        int posicion = 0;

        for (String valor : primero) {

            resultado[posicion] = valor;
            posicion++;
        }

        for (String valor : segundo) {

            resultado[posicion] = valor;
            posicion++;
        }

        for (String valor : tercero) {

            resultado[posicion] = valor;
            posicion++;
        }

        return resultado;
    }
    
    private String[] unirCuatroArreglos(
        String[] primero,
        String[] segundo,
        String[] tercero,
        String[] cuarto) {

    String[] resultado = new String[
            primero.length
            + segundo.length
            + tercero.length
            + cuarto.length
    ];

    int posicion = 0;

    for (String valor : primero) {
        resultado[posicion++] = valor;
    }

    for (String valor : segundo) {
        resultado[posicion++] = valor;
    }

    for (String valor : tercero) {
        resultado[posicion++] = valor;
    }

    for (String valor : cuarto) {
        resultado[posicion++] = valor;
    }

    return resultado;
}
}