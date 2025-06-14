package com.example.demo7.entity;

import java.util.Date;

public class StuCou {
    private String cno;
    private String sno;
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

    public void setEvaluation(String evaluation) {
        this.evaluation = evaluation;
    }
}
