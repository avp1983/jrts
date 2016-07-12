package com.bssys.ejb;

import com.bssys.process.step.ProcessStepResult;
import com.bssys.rts.RtsConnector;
import org.apache.commons.mail.SimpleEmail;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@Stateless
public class AdminNotificationBean {
  private static final int DEFAULT_SMTP_PORT = 25;
  @Inject
  private RtsConnector rtsConnector;

  @Inject
  private UMTSettingsConstantFacade settings;

  public void sendEmailToAdmin(ProcessStepResult checkResponseResult) {
    String adminMail = settings.getStringUMTSettingsByName("Settings", "AdminEMail", "");
    if (adminMail.isEmpty()) {
      throw new RuntimeException("В настройках не указан почтовый адрес администратора");
    }

    try {
      sendSimpleMail(adminMail, checkResponseResult.getHeader(), checkResponseResult.getDescription());
    }
    catch (Exception e) {
      throw new RuntimeException("Ошибка при отпавке email администратору");
    }
  }

  public void sendEmailToTechSupport(ProcessStepResult checkResponseResult) {
    String supportMail = settings.getStringUMTSettingsByName("Settings", "SupportServiceEMail", "");
    if (supportMail.isEmpty()) {
      throw new RuntimeException("В настройках не указан почтовый адрес службы поддержки");
    }

    try {
      sendSimpleMail(supportMail, checkResponseResult.getHeader(), checkResponseResult.getDescription());
    }
    catch (Exception e) {
      throw new RuntimeException("Ошибка при отпавке email службе поддержки");
    }
  }

  public void sendSimpleMail(String mail, String subject, String message) throws Exception {
    String smtpServer = settings.getStringUMTSettingsByName("Settings", "SMTP", "");
    if (smtpServer.isEmpty()) {
      throw new RuntimeException("Не указан адрес почтового сервера");
    }

    SimpleEmail email = new SimpleEmail();
    email.setHostName(smtpServer);
    email.setSmtpPort(DEFAULT_SMTP_PORT);
    email.setFrom(mail);
    email.addTo(mail);
    email.setSubject(subject);
    email.setMsg(message);
    email.send();
  }
}
