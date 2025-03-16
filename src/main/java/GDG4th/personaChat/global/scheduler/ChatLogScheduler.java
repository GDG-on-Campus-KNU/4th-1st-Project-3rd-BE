package GDG4th.personaChat.global.scheduler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ChatLogScheduler {
    @Scheduled(cron = "0 0 6,12,18,0 * * ?")
    public void scheduledTask() {
    }
}
