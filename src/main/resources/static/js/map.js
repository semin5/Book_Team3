document.addEventListener("DOMContentLoaded", function () {
    kakao.maps.load(function () {
        const container = document.getElementById('map');
        const options = {
            center: new kakao.maps.LatLng(36.5, 127.5),
            level: 13
        };
        const map = new kakao.maps.Map(container, options);

        const infowindows = [];

        locations.forEach(loc => {
            const position = new kakao.maps.LatLng(loc.latitude, loc.longitude);
            const marker = new kakao.maps.Marker({
                map: map,
                position: position
            });

            const isSolvedText = loc.isSolved ? "✅ 완료" : "❓ 답변 대기";

            const content = `
                <div style="padding: 10px; min-width: 250px; width: max-content; font-size: 14px; font-family: 'Apple SD Gothic Neo', sans-serif; line-height: 1.5; white-space: nowrap;">
                    <div><strong>주소:</strong> ${loc.address}</div>
                    <div><strong>제목:</strong> ${loc.title}</div>
                    <div><strong>상태:</strong> ${isSolvedText}</div>
                     ${loc.isSolved && loc.bookTitle
                        ? `<div><strong>추천 책:</strong> ${loc.bookTitle}</div>
                           <div><strong>저자:</strong> ${loc.bookAuthor}</div>`
                        : ''
                    }
                    <div class="mt-2" style="text-align: right;">
                        <a href="/posts/${loc.postId}" class="btn btn-sm btn-primary">게시글로 이동</a>
                    </div>
                </div>
            `;

            const infowindow = new kakao.maps.InfoWindow({
                content: content,
                disableAutoPan: true
            });
            infowindows.push(infowindow);

            let isOpen = false;

            kakao.maps.event.addListener(marker, 'click', function () {
                if (isOpen) {
                    infowindow.close();
                    isOpen = false;
                } else {
                    infowindows.forEach(iw => iw.close());
                    infowindow.open(map, marker);
                    isOpen = true;
                }
            });
        });
    });
});