document.getElementById('saveUidButton').addEventListener('click', () => {
    const uid = document.getElementById('uidInput').value.trim();
    if (uid) {
        chrome.runtime.sendMessage({ action: 'setUid', uid: uid }, (response) => {
            document.getElementById('statusMessage').textContent = response.status;
        });
    } else {
        document.getElementById('statusMessage').textContent = 'Please enter a UID';
    }
});