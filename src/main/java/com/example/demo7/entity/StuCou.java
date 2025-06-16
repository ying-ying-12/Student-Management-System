package com.example.demo7.entity;

import java.util.Date;

public class StuCou {
    private String cno;
    private String cname;
    private String sno;
    private String sname;
    private Date chosetime;
    private Double score;
    private String evaluation;

    public String getCno() {
        return cno;
    }

    public void setCno(String cno) {
        this.cno = cno;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public Date getChosetime() {
        return chosetime;
    }

    public void setChosetime(Date chosetime) {
        this.chosetime = chosetime;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getEvaluation() {
        return evaluation;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public void setEvaluation(String evaluation) {
        this.evaluation = evaluation;
    }
}
