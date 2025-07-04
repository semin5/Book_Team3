document.addEventListener("DOMContentLoaded", function () {
    kakao.maps.load(function () {
        let lat = parseFloat(document.getElementById('latitude').value);
        let lng = parseFloat(document.getElementById('longitude').value);

        if (!isNaN(lat) && !isNaN(lng)) {
            let mapContainer = document.getElementById('map');
            let mapOption = {
                center: new kakao.maps.LatLng(lat, lng),
                level: 3
            };
            let map = new kakao.maps.Map(mapContainer, mapOption);

            new kakao.maps.Marker({
                map: map,
                position: new kakao.maps.LatLng(lat, lng)
            });
        } else {
            document.getElementById('map').innerHTML = "<p class='text-danger'>등록된 위치 정보가 없습니다.</p>";
        }
    });
});