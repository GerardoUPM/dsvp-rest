package edu.ctb.upm.midas.model.wikipediaApi;

import java.util.List;
import java.util.Objects;

public class Revision {

    private Integer revid;
    private Integer parentid;
    private Integer size;
    private String user;
    private Integer userid;
    private String timestamp;
    private String date;
    private String previousDate;
    private String snapshot;
    private String comment;
    private boolean minor;
    private Integer relevantSectionCount;
    private Integer sectionCount;
    private List<Section> sections;
    private Long characterCount;//Número de carateres de todos los textos de cada sección
    private String text;
    private Integer referenceCount;
    private Integer sentenceCount;



    public Revision() {
    }

    public Integer getRevid() {
        return revid;
    }

    public void setRevid(Integer revid) {
        this.revid = revid;
    }

    public Integer getParentid() {
        return parentid;
    }

    public void setParentid(Integer parentid) {
        this.parentid = parentid;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPreviousDate() {
        return previousDate;
    }

    public void setPreviousDate(String previousDate) {
        this.previousDate = previousDate;
    }

    public String getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(String snapshot) {
        this.snapshot = snapshot;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isMinor() {
        return minor;
    }

    public void setMinor(boolean minor) {
        this.minor = minor;
    }

    public Integer getRelevantSectionCount() {
        return relevantSectionCount;
    }

    public void setRelevantSectionCount(Integer relevantSectionCount) {
        this.relevantSectionCount = relevantSectionCount;
    }

    public Integer getSectionCount() {
        return sectionCount;
    }

    public void setSectionCount(Integer sectionCount) {
        this.sectionCount = sectionCount;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }

    public Long getCharacterCount() {
        return characterCount;
    }

    public void setCharacterCount(Long characterCount) {
        this.characterCount = characterCount;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getReferenceCount() {
        return referenceCount;
    }

    public void setReferenceCount(Integer referenceCount) {
        this.referenceCount = referenceCount;
    }

    public Integer getSentenceCount() {
        return sentenceCount;
    }

    public void setSentenceCount(Integer sentenceCount) {
        this.sentenceCount = sentenceCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Revision)) return false;
        Revision revision = (Revision) o;
        return Objects.equals(getRevid(), revision.getRevid());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getRevid());
    }

    @Override
    public String toString() {
        return "\nRevision{" +
                "\n revid=" + revid +
                "\n , parentid=" + parentid +
                "\n , size=" + size +
                "\n , user='" + user + '\'' +
                "\n , userid=" + userid +
                "\n , timestamp='" + timestamp + '\'' +
                "\n , date='" + date + '\'' +
                "\n , previousDate='" + previousDate + '\'' +
                "\n , comment='" + comment + '\'' +
                "\n , minor=" + minor +
//                "\n , sections='" + sections.size() + '\'' +
//                "\n , characterCount='" + characterCount + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
