package com.hatchworks.cvparser;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class CvparserApplicationTests {

    // -------------------------------------------------------------------------
    // TEST 1 - VERIFICAR QUE SPRING BOOT CARGA CORRECTAMENTE
    // -------------------------------------------------------------------------

    @Test
    void contextLoads() {
    }

    // -------------------------------------------------------------------------
    // TEST 2 - CV EN INGLES
    // -------------------------------------------------------------------------

    @Test
    void shouldExtractEnglishCv() {

        String texto = """
                Michael Carter
                michael.carter@example.com
                +1 512 555 2846

                Education
                Bachelor of Science in Computer Science
                Central State University
                2022 - Present

                Technical Skills
                Java
                Spring Boot
                SQL
                Git

                Professional Experience
                Junior Backend Developer
                BrightCode Systems
                2025 - Present
                """;

        CvParser parser = new CvParser();

        CvData resultado = parser.parse(texto);

        assertEquals(
                "Michael Carter",
                resultado.getName()
        );

        assertEquals(
                "michael.carter@example.com",
                resultado.getEmail()
        );

        assertTrue(
                resultado.getEducation().contains(
                        "Bachelor of Science in Computer Science"
                )
        );

        assertTrue(
                resultado.getSkills().contains("Java")
        );

        assertTrue(
                resultado.getExperience().contains(
                        "Junior Backend Developer"
                )
        );
    }

    // -------------------------------------------------------------------------
    // TEST 3 - CV EN ESPANOL
    // -------------------------------------------------------------------------

    @Test
    void shouldExtractSpanishCv() {

        String texto = """
                Laura Jimenez Vargas
                laura.jimenez@example.com
                +506 8712 3490

                Formacion Academica
                Bachillerato en Ingenieria Informatica
                Universidad Nacional de Tecnologia
                2023 - Actualidad

                Habilidades Tecnicas
                Java
                SQL
                Power BI
                Excel

                Experiencia Laboral
                Analista de Datos Junior
                Data Solutions CR
                Enero 2025 - Actualidad
                """;

        CvParser parser = new CvParser();

        CvData resultado = parser.parse(texto);

        assertEquals(
                "Laura Jimenez Vargas",
                resultado.getName()
        );

        assertEquals(
                "laura.jimenez@example.com",
                resultado.getEmail()
        );

        assertTrue(
                resultado.getEducation().contains(
                        "Bachillerato en Ingenieria Informatica"
                )
        );

        assertTrue(
                resultado.getSkills().contains("Power BI")
        );

        assertTrue(
                resultado.getExperience().contains(
                        "Analista de Datos Junior"
                )
        );
    }

    // -------------------------------------------------------------------------
    // TEST 4 - CAMPO NO DETECTADO
    // -------------------------------------------------------------------------

    @Test
    void shouldReturnNotDetectedWhenEmailIsMissing() {

        String texto = """
                Sofia Rodriguez

                Education
                Computer Science

                Skills
                Java
                SQL

                Professional Experience
                Software Intern
                """;

        CvParser parser = new CvParser();

        CvData resultado = parser.parse(texto);

        assertEquals(
                "Not detected",
                resultado.getEmail()
        );
    }
}