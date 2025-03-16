package GDG4th.personaChat.global.scheduler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ChatLogScheduler {
    @Value("${scheduler.cron}") // 프로퍼티에서 크론 표현식 가져오기
    private String cronExpression;

    @Scheduled(cron = "${scheduler.cron}")
    public void scheduledTask() {
    }
}
