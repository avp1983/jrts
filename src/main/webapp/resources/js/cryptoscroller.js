function onClickCryptoProfiles() {
    loadByTask('RT_2IC.form', 'CRYPTO', 'NEW', null, 'cryptoscrollertab');
}
function onClickCryptoGetCertificate() {
    loadScroller('CRYPTO', 'REGISTERED', 'cryptoscrollertab');
}
function onClickCryptoRejected() {
    loadScroller('CRYPTO', 'REJECTEDCRYPTO', 'cryptoscrollertab');
}
function onClickCryptoAll() {
    loadScroller('CRYPTO', 'ALL', 'cryptoscrollertab');
}