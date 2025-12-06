package com.danspec.danspec.mail.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    public void sendVerificationCode(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("[단스펙] 이메일 인증번호");
        message.setText("안녕하세요, 단스펙입니다.\n\n" +
                "이메일 인증번호는 다음과 같습니다:\n\n" +
                code + "\n\n" +
                "인증번호는 5분간 유효합니다.\n" +
                "본인이 요청한 것이 아니라면 무시하셔도 됩니다.");
        mailSender.send(message);
    }
}
