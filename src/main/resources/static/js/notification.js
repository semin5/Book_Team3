document.addEventListener("DOMContentLoaded", function () {
    if (typeof memberId !== 'undefined') {
        const eventSource = new EventSource(`/notifications/connect?memberId=${memberId}`);

        eventSource.onopen = function () {
            console.log("✅ SSE 연결 성공");
        };

        eventSource.addEventListener("notification", function (event) {
            const notification = JSON.parse(event.data);
            console.log("🔔 새 알림:", notification);

            const box = document.getElementById("notification-box");
            const item = document.createElement("div");
            item.className = "alert alert-info";

            const link = document.createElement("a");
            link.href = `/posts/${notification.postId}`;
            link.className = "text-decoration-none text-dark";
            link.innerText = `[알림] ${notification.content}`;

            item.appendChild(link);
            box.prepend(item);
        });

        eventSource.onerror = function (err) {
            console.error("❌ SSE 연결 오류", err);
            eventSource.close();
        };
    }
});