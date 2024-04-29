'use strict'

// グローバグ変数はこれ以外使わない
const App = {}

// .notification のx ボタンをクリックしたら閉じる
window.addEventListener('load', () => {
    const deleteButtonList = document.querySelectorAll('.notification>.delete')
    for (let i = 0; i < deleteButtonList.length; i++) {
        const button = deleteButtonList[i]
        button.addEventListener("click", (event) => {
            button.parentNode.style.display='none'
        })
    }
})

// toggle の開閉制御
App.refreshToggleArea = function(toggle, willBeClose) {
    // switch についているdata-toggle-key からキー名称を取得
    const key = toggle.getAttribute('data-toggle-key')
    // 同じキーを持つ領域を開閉制御
    const toggleAreaList = document.querySelectorAll('[data-toggle-key=' + key + ']')
    for (let j = 0; j < toggleAreaList.length; j++) {
        const area = toggleAreaList[j]
        if (willBeClose) {
          area.classList.remove('app-toggle-open')
        } else {
          area.classList.add('app-toggle-open')
        }
    }
}

// toggle スイッチの有効化
//   スイッチ要素
//     class="app-toggle-switch" data-toggle-key="xxx"
//   開閉領域
//     class="app-toggle" data-toggle-key="xxx"
//   ※xxx が同じ領域を開閉する
window.addEventListener('load', () => {
    const toggleSwitchList = document.querySelectorAll('.app-toggle-switch')
    for (let i = 0; i < toggleSwitchList.length; i++) {
        const toggle = toggleSwitchList[i]
        // クリックイベントを設定
        toggle.addEventListener("click", (event) => {
            // switch にopen が付いているなら開いていると判断
            const classList = toggle.classList;
            const willBeClose = classList.contains('app-toggle-open')
            // open がついていたら付ける、なければ削除
            classList.toggle('app-toggle-open')
            // 開閉状態を制御
            App.refreshToggleArea(toggle, willBeClose)
        })
        // 現状に合わせて開閉状態を制御
        const willBeClose = toggle.classList.contains('app-toggle-open')
        App.refreshToggleArea(toggle, !willBeClose)
    }
})

// ページ変更
App.pageChange = function(linkEl, page) {
    const navKeyEl = linkEl.closest('nav[data-pagination-key]')
    // key の指定があれば検索条件に追加する
    // 1ページに複数のpagination を置いた場合の対応
    const key = navKeyEl && navKeyEl.getAttribute('data-pagination-key') || ''
    const additionalQuery = key ? '[data-pagination-key=' + key + ']' : ''
    const pageInput = document.querySelector('input[type=hidden]' + additionalQuery)
    if (pageInput) {
        pageInput.value = page
        pageInput.form && pageInput.form.submit()
    }
}
window.addEventListener('load', () => {
    // ページクリック
    const linkList = document.querySelectorAll('.pagination .pagination-link')
    for (let i = 0; i < linkList.length; i++) {
        const link = linkList[i]
        // クリックイベントを設定
        link.addEventListener("click", (event) => {
            App.pageChange(link, Number(link.innerHTML))
        })
    }
})
