document.addEventListener("DOMContentLoaded", function () {
    kakao.maps.load(function () {
        let mapContainer = document.getElementById('map');
        let mapOption = {
            center: new kakao.maps.LatLng(37.5665, 126.9780),
            level: 3
        };
        let map = new kakao.maps.Map(mapContainer, mapOption);
        let geocoder = new kakao.maps.services.Geocoder();
        let marker = new kakao.maps.Marker();

        document.getElementById("btn-address").addEventListener("click", function () {
            const address = document.getElementById('address').value;

            geocoder.addressSearch(address, function(result, status) {
                if (status === kakao.maps.services.Status.OK) {
                    let coords = new kakao.maps.LatLng(result[0].y, result[0].x);
                    map.setCenter(coords);

                    marker.setMap(null);
                    marker = new kakao.maps.Marker({
                        map: map,
                        position: coords
                    });

                    document.getElementById('latitude').value = result[0].y;
                    document.getElementById('longitude').value = result[0].x;
                } else {
                    alert('주소를 찾을 수 없습니다.');
                }
            });
        });
        kakao.maps.event.addListener(map, 'click', function(mouseEvent) {
            let latlng = mouseEvent.latLng;

            geocoder.coord2Address(latlng.getLng(), latlng.getLat(), function(result, status) {
                if (status === kakao.maps.services.Status.OK) {
                    let address = result[0].address.address_name;

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