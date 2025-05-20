package com.example.auth_service.dto;

public class ProfileRoleDTO {

    private Long id;
    private Long profileId;
    private String profileName;
    private Long roleId;
    private String roleNameEn;
    private String roleNameAr;
    private boolean showLecture;
    private boolean showEcriture;
    private boolean showTous;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleNameEn() {
        return roleNameEn;
    }

    public void setRoleNameEn(String roleNameEn) {
        this.roleNameEn = roleNameEn;
    }

    public String getRoleNameAr() {
        return roleNameAr;
    }

    public void setRoleNameAr(String roleNameAr) {
        this.roleNameAr = roleNameAr;
    }

    public boolean isShowLecture() {
        return showLecture;
    }

    public void setShowLecture(boolean showLecture) {
        this.showLecture = showLecture;
    }

    public boolean isShowEcriture() {
        return showEcriture;
    }

    public void setShowEcriture(boolean showEcriture) {
        this.showEcriture = showEcriture;
    }

    public boolean isShowTous() {
        return showTous;
    }

    public void setShowTous(boolean showTous) {
        this.showTous = showTous;
    }
}
