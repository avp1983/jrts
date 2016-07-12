package com.bssys.entity;

import com.bssys.bls.types.DboRootDocument;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by e.zemtcovsky on 26.02.2015.
 */
@Entity
@NamedQueries({
  @NamedQuery(name = "ExprRepRequest.getByPK", query = "SELECT e FROM ExprRepRequest e WHERE " +
    "e.docPK = :docPK")
})
@Cacheable(false)
public class ExprRepRequest extends DboRootDocument {

  private Integer reportTypeId;


  @Column(name = "ReportTypeId")
  public Integer getReportTypeId() {
    return reportTypeId;
  }

  public void setReportTypeId(Integer reportTypeId) {
    this.reportTypeId = reportTypeId;
  }

  private Integer reportId;


  @Column(name = "ReportId")
  public Integer getReportId() {
    return reportId;
  }

  public void setReportId(Integer reportId) {
    this.reportId = reportId;
  }

  private byte[] reportparams;


  @Column(name = "REPORTPARAMS")
  public byte[] getReportparams() {
    return reportparams;
  }

  public void setReportparams(byte[] reportparams) {
    this.reportparams = reportparams;
  }

  private byte[] readyreport;

  @Column(name = "READYREPORT")
  public byte[] getReadyreport() {
    return readyreport;
  }

  public void setReadyreport(byte[] readyreport) {
    this.readyreport = readyreport;
  }


}
