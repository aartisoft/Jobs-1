package nithra.jobs.career.placement.pojo;

import android.widget.FrameLayout;

/**
 * Created by arunrk on 24/5/17.
 */

public class Jobs {

    public FrameLayout frameLayout;
    public boolean isAlarm, isAd, isRead;
    public int id, iscomplete, datediff, isAMPM;
    public String postedby;
    public String jobtitle;
    public String image;
    public String jobtype;
    public String experience;
    public String date;
    public String jobcat;
    public String employer;
    public String type;
    public String description;
    public String skills;
    public String qualification;
    public String noofvacancy;
    public String employerid;
    public String district;
    public String taluk;
    public String salary;
    public String posteddate;
    public String expiredate;
    public String completeddate;
    public String verified;
    public long actionDate;

    public Jobs() {}

    public Jobs(int id, String image, String jobtitle, String employer, String date, long actionDate) {
        this.id = id;
        this.image = image;
        this.jobtitle = jobtitle;
        this.employer = employer;
        this.date = date;
        this.actionDate = actionDate;
    }

    public boolean isAd() {
        return isAd;
    }

    public void setAd(boolean ad) {
        isAd = ad;
    }

    public long getActionDate() {
        return actionDate;
    }

    public void setActionDate(long actionDate) {
        this.actionDate = actionDate;
    }

    public int getDatediff() {
        return datediff;
    }

    public void setDatediff(int datediff) {
        this.datediff = datediff;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getJobtype() {
        return jobtype;
    }

    public void setJobtype(String jobtype) {
        this.jobtype = jobtype;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEmployer() {
        return employer;
    }

    public void setEmployer(String employer) {
        this.employer = employer;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJobtitle() {
        return jobtitle;
    }

    public void setJobtitle(String jobtitle) {
        this.jobtitle = jobtitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getNoofvacancy() {
        return noofvacancy;
    }

    public void setNoofvacancy(String noofvacancy) {
        this.noofvacancy = noofvacancy;
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public int getIsAMPM() {
        return isAMPM;
    }

    public void setIsAMPM(int isAMPM) {
        this.isAMPM = isAMPM;
    }

    public String getJobcat() {
        return jobcat;
    }

    public void setJobcat(String jobcat) {
        this.jobcat = jobcat;
    }

    public String getEmployerid() {
        return employerid;
    }

    public void setEmployerid(String employerid) {
        this.employerid = employerid;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getTaluk() {
        return taluk;
    }

    public void setTaluk(String taluk) {
        this.taluk = taluk;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getPosteddate() {
        return posteddate;
    }

    public void setPosteddate(String posteddate) {
        this.posteddate = posteddate;
    }

    public String getExpiredate() {
        return expiredate;
    }

    public void setExpiredate(String expiredate) {
        this.expiredate = expiredate;
    }

    public String getCompleteddate() {
        return completeddate;
    }

    public void setCompleteddate(String completeddate) {
        this.completeddate = completeddate;
    }

    public FrameLayout getFrameLayout() {
        return frameLayout;
    }

    public void setFrameLayout(FrameLayout frameLayout) {
        this.frameLayout = frameLayout;
    }

    public String getPostedby() {
        return postedby;
    }

    public void setPostedby(String postedby) {
        this.postedby = postedby;
    }

    public int getIscomplete() {
        return iscomplete;
    }

    public void setIscomplete(int iscomplete) {
        this.iscomplete = iscomplete;
    }

    public boolean isAlarm() {
        return isAlarm;
    }

    public void setAlarm(boolean alarm) {
        isAlarm = alarm;
    }
}
