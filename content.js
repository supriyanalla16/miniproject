
chrome.runtime.sendMessage({ action: 'setUid', uid: 'example-uid' }, (response) => {
    console.log(response.status);
});