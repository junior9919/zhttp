package com.halo;

public class MedalRecordBean {

    private Integer id;

    private String winningDate;

    private String medalWinner;

    private String medalClause;

    private String medalName;

    private Short isWinning;

    private String comment;

    private String presenter;

    private String reviewer;

    private String medalImage;

    private Short isNew;

    public MedalRecordBean() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWinningDate() {
        return winningDate;
    }

    public void setWinningDate(String winningDate) {
        this.winningDate = winningDate;
    }

    public String getMedalWinner() {
        return medalWinner;
    }

    public void setMedalWinner(String medalWinner) {
        this.medalWinner = medalWinner;
    }

    public String getMedalClause() {
        return medalClause;
    }

    public void setMedalClause(String medalClause) {
        this.medalClause = medalClause;
    }

    public String getMedalName() {
        return medalName;
    }

    public void setMedalName(String medalName) {
        this.medalName = medalName;
    }

    public Short getIsWinning() {
        return isWinning;
    }

    public void setIsWinning(Short isWinning) {
        this.isWinning = isWinning;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPresenter() {
        return presenter;
    }

    public void setPresenter(String presenter) {
        this.presenter = presenter;
    }

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public String getMedalImage() {
        return medalImage;
    }

    public void setMedalImage(String medalImage) {
        this.medalImage = medalImage;
    }

    public Short getIsNew() {
        return isNew;
    }

    public void setIsNew(Short isNew) {
        this.isNew = isNew;
    }
}
