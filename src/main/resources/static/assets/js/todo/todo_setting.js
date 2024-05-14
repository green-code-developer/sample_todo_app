window.addEventListener('load', () => {
  document.querySelectorAll('#dark-mode').forEach(el => {
    el.addEventListener('change', () => {
      localStorage.setItem("dark-mode", el.value)
      App.refreshDarkMode()
    })
    el.value = localStorage.getItem("dark-mode") ?? ''
  })
})