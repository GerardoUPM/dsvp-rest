package edu.ctb.upm.midas.model.wikipediaApi;

import java.util.List;

public class Page {

    private Integer pageid;
    private String title;
    private boolean isredirect;
    private Integer redirectpageid;
    private String redirectpagetitle;
    private Integer revisionCount;
    private List<Revision> revisions;


    public Page() {
    }

    public Page(Integer pageid, String title) {
        this.pageid = pageid;
        this.title = title;
    }

    public Page(Integer pageid, String title, List<Revision> revisions) {
        this.pageid = pageid;
        this.title = title;
        this.revisions = revisions;
    }



    public Integer getPageid() {
        return pageid;
    }

    public void setPageid(Integer pageid) {
        this.pageid = pageid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getRevisionCount() {
        return revisionCount;
    }

    public void setRevisionCount(Integer revisionCount) {
        this.revisionCount = revisionCount;
    }

    public List<Revision> getRevisions() {
        return revisions;
    }

    public void setRevisions(List<Revision> revisions) {
        this.revisions = revisions;
    }

    public boolean isIsredirect() {
        return isredirect;
    }

    public void setIsredirect(boolean isredirect) {
        this.isredirect = isredirect;
    }

    public Integer getRedirectpageid() {
        return redirectpageid;
    }

    public void setRedirectpageid(Integer redirectpageid) {
        this.redirectpageid = redirectpageid;
    }

    public String getRedirectpagetitle() {
        return redirectpagetitle;
    }

    public void setRedirectpagetitle(String redirectpagetitle) {
        this.redirectpagetitle = redirectpagetitle;
    }


    @Override
    public String toString() {
        return "\nPage{" +
                "\npageid=" + pageid +
                "\n, title='" + title + '\'' +
                "\n, revisions=" + revisions +
                "\n, isredirect=" + isredirect +
                "\n, redirectpageid=" + redirectpageid +
                "\n, redirectpagetitle='" + redirectpagetitle + '\'' +
                '}';
    }
}
