export function setLocal (key, value) {
    window.localStorage.setItem(key, JSON.stringify(value))
}
export function getLocal (key) {
    const value = window.localStorage.getItem(key)
    try {
        return JSON.parse(window.localStorage.getItem(key))
    } catch (error) {
        return value
    }
}
export function removeLocal (key) {
    window.localStorage.removeItem(key)
}
export function clearLocalAll () {
    window.localStorage.clear();
}