
App.pageLoadEvent = function() {
    // クリアリンク
    const clearLink = document.querySelector('#clear')
    clearLink.addEventListener("click", (event) => {
        const form = document.querySelector('form')
        const inputList = form.querySelectorAll('input')
        // input タグのhidden でないものに空文字列をセット
        for (let i = 0; i < inputList.length; i++) {
            const input = inputList[i]
            if (input.type != 'hidden') {
                input.value = ''
            }
        }
        // select タグは一番上の選択肢を選択
        const selectList = form.querySelectorAll('select')
        for (let i = 0; i < selectList.length; i++) {
            const select = selectList[i]
            select.selectedIndex = 0
        }
        // 1ページ件数のみ10件を選択
        form.querySelectorAll('[name=pageSize]').forEach(el => el.value = 10)
    })
}
window.addEventListener('load', App.pageLoadEvent);