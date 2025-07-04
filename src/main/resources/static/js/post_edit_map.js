document.addEventListener("DOMContentLoaded", function () {
    kakao.maps.load(function () {
        const mapContainer = document.getElementById('map');
        const lat = parseFloat(document.getElementById("latitude").value) || 37.5665;
        const lng = parseFloat(document.getElementById("longitude").value) || 126.9780;

        const mapOption = {
            center: new kakao.maps.LatLng(lat, lng),
            level: 3
        };

        const map = new kakao.maps.Map(mapContainer, mapOption);
        const geocoder = new kakao.maps.services.Geocoder();
        let marker = new kakao.maps.Marker({
            position: new kakao.maps.LatLng(lat, lng),
            map: map
        });

        document.getElementById("btn-address").addEventListener("click", function () {
            const address = document.getElementById("address").value;
            geocoder.addressSearch(address, function(result, status) {
                if (status === kakao.maps.services.Status.OK) {
                    const coords = new kakao.maps.LatLng(result[0].y, result[0].x);
                    map.setCenter(coords);
                    marker.setMap(null);
                    marker = new kakao.maps.Marker({ map: map, position: coords });

                    document.getElementById("latitude").value = result[0].y;
                    document.getElementById("longitude").value = result[0].x;
                } else {
                    alert('주소를 찾을 수 없습니다.');
                }
            });
        });

        kakao.maps.event.addListener(map, 'click', function(mouseEvent) {
            const latlng = mouseEvent.latLng;

            geocoder.coord2Address(latlng.getLng(), latlng.getLat(), function(result, status) {
                if (status === kakao.maps.services.Status.OK) {
                    const address = result[0].address.address_name;
                    marker.setMap(null);
                    marker = new kakao.maps.Marker({
                        position: latlng,
                        map: map
                    });

                    document.getElementById("address").value = address;
                    document.getElementById("latitude").value = latlng.getLat();
                    document.getElementById("longitude").value = latlng.getLng();
                }
            });
        });
    });
});