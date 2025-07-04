function checkFileCount(input) {
    if (input.files.length > 2) {
        alert("최대 2개까지만 업로드할 수 있습니다.");
        input.value = '';
    }
}