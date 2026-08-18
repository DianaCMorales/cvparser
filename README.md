# CV Parser & Reimagined Profile Viewer

Technical Challenge for the HatchWorks AI Internship.

## Live Application

The application is publicly deployed on Render:

https://cvparser-x16g.onrender.com

## Overview

CV Parser & Reimagined Profile Viewer is a web application that allows users to upload a CV in PDF format, extract relevant information from the document, and display it in a redesigned professional profile.

The application focuses on transforming unstructured CV content into structured information while presenting the extracted data in a visual format that is different from the original document.

## Features

- Upload CVs in PDF format.
- Extract text from PDF documents.
- Extract structured information including:
  - Name
  - Email
  - Phone number
  - Education
  - Skills
  - Professional experience
- Support for different CV structures and section headings.
- Support for CVs in English and Spanish.
- Handle multi-page PDF documents.
- Display `Not detected` when a field cannot be reliably extracted.
- Display the extracted information in a redesigned profile view.
- Download the redesigned professional profile as a PDF.
- Basic validation and error handling for file uploads.

## Technologies Used

- Java 21
- Spring Boot
- Maven
- Apache PDFBox
- Thymeleaf
- HTML
- CSS
- Regular Expressions (Regex)
- OpenHTMLtoPDF

## Architecture

The application follows a simple Spring Boot architecture:

- **CvController**: Handles HTTP requests, PDF uploads, profile rendering, and PDF downloads.
- **CvParser**: Contains the extraction logic used to identify structured CV information.
- **CvData**: Data model that stores the extracted fields.
- **Thymeleaf Templates**: Render the upload page and redesigned profile.
- **CSS**: Provides the visual design and layout of the application.
- **PDFBox**: Extracts raw text from uploaded PDF documents.
- **OpenHTMLtoPDF**: Generates the downloadable redesigned profile PDF.

### Application Flow

```text
Upload PDF
    |
    v
Extract text with PDFBox
    |
    v
Parse text using regex and heuristics
    |
    v
Create structured CvData
    |
    v
Render redesigned profile with Thymeleaf
    |
    v
Download redesigned profile as PDF
```

## Data Extraction Approach

The application uses a combination of PDF text extraction, regular expressions, and heuristic rules.

Apache PDFBox is used to obtain the raw text from PDF files.

Regular expressions are used to detect structured contact information such as email addresses and phone numbers.

For sections such as education, skills, and professional experience, the parser searches for multiple possible section headings.

Examples include:

**Education**
- Education
- Academic Background
- Education and Training
- Educación
- Formación Académica

**Skills**
- Skills
- Technical Skills
- Core Skills
- Key Skills
- Habilidades
- Habilidades Técnicas

**Experience**
- Professional Experience
- Work Experience
- Experience
- Employment History
- Experiencia Profesional
- Experiencia Laboral

The parser identifies where a section starts and stops based on these headings. This approach allows the application to process CVs with different structures without depending on one fixed template.

### Trade-offs

A heuristic and regex-based approach was selected because it is lightweight, explainable, does not require external AI services, and can run without API keys or additional usage costs.

The trade-off is that CVs are highly variable. Documents with unusual headings, complex multi-column layouts, scanned images, or unconventional formatting may result in fields being partially extracted or marked as `Not detected`.

The goal of the implementation is therefore reasonable extraction across common real-world CV formats rather than guaranteed extraction from every possible layout.

## English and Spanish Support

The parser supports common section headings in both English and Spanish.

This was tested with CVs using different structures and terminology in both languages. Additional heading variants can be added to the parser as needed.

## Error Handling

The application includes fallback behavior for missing or unrecognized information.

If a field cannot be extracted, the application displays:

```text
Not detected
```

The upload flow also validates that a file has been selected and handles processing errors instead of intentionally presenting blank extracted fields.

## Requirements

To run the project locally:

- Java 21 or compatible JDK
- Maven, or the included Maven Wrapper
- A modern web browser

## Running the Project Locally

### Windows

Clone the repository:

```bash
git clone https://github.com/DianaCMorales/cvparser.git
```

Enter the project directory:

```bash
cd cvparser
```

Run the application using the included Maven Wrapper:

```bash
mvnw.cmd spring-boot:run
```

Then open:

```text
http://localhost:8080
```

### Using Maven

If Maven is already installed:

```bash
mvn spring-boot:run
```

## Known Limitations

- Currently supports PDF input only.
- Scanned PDFs that contain only images are not supported because OCR is not implemented.
- Extraction accuracy depends on the structure and text representation of the uploaded PDF.
- Highly complex multi-column layouts may affect the order in which PDFBox extracts text.
- Unusual section headings that are not included in the parser may not be detected.
- Phone number detection focuses on common formats and may not recognize every international format.
- The application does not currently allow users to manually edit extracted information before rendering the final profile.

## Possible Improvements

With more development time, the project could include:

- DOCX support.
- OCR for scanned CVs and image-based documents.
- User review and editing of extracted fields before generating the final profile.
- Additional CV section-heading variations.
- More robust international phone-number detection.
- Confidence indicators for extracted fields.
- Additional downloadable formats such as PNG.
- Automated unit tests covering additional extraction edge cases.
- AI/LLM-assisted semantic extraction as an optional fallback for highly irregular CV layouts.

## AI-Assisted Development

AI tools were used as a development assistant during the project for tasks such as discussing implementation approaches, debugging, reviewing edge cases, and suggesting improvements.

The implementation was built, tested, reviewed, and iteratively adjusted by the developer. The extraction behavior was validated using multiple CV samples with different layouts and both English and Spanish content.

## Author

**Diana Morales Fuentes**

Computer Engineering / Informatics Engineering Student

## Challenge

HatchWorks AI Internship — Intern Technical Challenge  
CV Parser & Reimagined Profile Viewer
