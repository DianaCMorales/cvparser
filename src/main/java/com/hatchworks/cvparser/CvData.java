//-------------------------------------------------------------------------------
//CREADOR: DIANA MORALES FUENTES CEDULA 40550101
//-------------------------------------------------------------------------------
package com.hatchworks.cvparser;
//-------------------------------------------------------------------------------
//ATRIBUTOS PRINCIPALES
//-------------------------------------------------------------------------------
public class CvData {
    
private String name;
private String email;
private String phone;
private String experience;
private String education;
private String skills;

//-----------------------------------------------------------------------------
//GETTERS Y SETTERS DE ATRIBUTOS DEL CV
//-------------------------------------------------------------------------------

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }
    
}
