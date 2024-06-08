chrome.runtime.onMessage.addListener((request, sender, sendResponse) => {
    if (request.action === 'setUid') {
        chrome.storage.local.set({ uid: request.uid }, () => {
            console.log('UID saved to local storage:', request.uid);
            sendResponse({ status: 'UID saved' });
        });
        return true; // Keeps the message channel open for sendResponse
    }
});

const negativeKeywords = ['depression', 'anxiety', 'suicide', 'stress', 'self-harm'];

chrome.history.onVisited.addListener((historyItem) => {
    for (const keyword of negativeKeywords) {
        if (historyItem.url.toLowerCase().includes(keyword) || historyItem.title.toLowerCase().includes(keyword)) {
            sendToAndroidApp(historyItem);
            break;
        }
    }
});

function sendToAndroidApp(historyItem) {
    chrome.storage.local.get(['uid'], (result) => {
        const uid = result.uid;
        if (uid) {
            fetch('http://127.0.0.1:5000/send_history', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    uid: uid, // Include the UID
                    url: historyItem.url,
                    title: historyItem.title,
                    lastVisitTime: historyItem.lastVisitTime
                })
            })
            .then(response => response.json())
            .then(data => console.log('Server response:', data))
            .catch(error => console.error('Error:', error));
        } else {
            console.error('UID not found in local storage.');
        }
    });
}
