package zafus.rubikbmt.rubikbmt_website.components;

import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class SessionListener implements HttpSessionListener {

    // Đếm số người truy cập hiện tại
    private AtomicInteger activeSessions = new AtomicInteger();

    // Đếm số người truy cập trong ngày
    private AtomicInteger dailyVisitors = new AtomicInteger();
    private LocalDate lastUpdated = LocalDate.now();

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        activeSessions.incrementAndGet();

        // Cập nhật lại đếm số người truy cập trong ngày nếu đã qua ngày mới
        if (!LocalDate.now().equals(lastUpdated)) {
            dailyVisitors.set(0);
            lastUpdated = LocalDate.now();
        }
        dailyVisitors.incrementAndGet();
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        activeSessions.decrementAndGet();
    }

    public int getActiveSessions() {
        return activeSessions.get();
    }

    public int getDailyVisitors() {
        return dailyVisitors.get();
    }
}