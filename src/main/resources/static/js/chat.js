var app = new Vue({
    el: '#app',
    data: {
        webSocket: null,
        user: {
            name: '匿名用户'
        },
        message: '',
        messages: [],
        onlineUsers: [],
        isChangingName: false,
        showMobileOnlineUser: false
    },
    created: function () {
        this.webSocket = new WebSocket('ws://' + window.location.host + '/chat')

        this.webSocket.onopen = (event) => {
            console.log('WebSocket打开连接')
        };

        this.webSocket.onmessage = (event) => {
            console.log('WebSocket收到消息：%c' + event.data, 'color:green')
            //获取服务端消息
            var message = JSON.parse(event.data) || {}
            if (message.type === 'ENTER' || message.type === 'SPEAK') {
                this.messages.push({
                    type: message.type,
                    user: message.user,
                    time: new Date().toLocaleString(),
                    message: message.message
                })
            }
            this.onlineUsers = message.onlineUsers
        };

        this.webSocket.onclose = (event) => {
            console.log('WebSocket关闭连接')
        };

        this.webSocket.onerror = (event) => {
            console.error('WebSocket发生异常')
        };
    },
    methods: {
        sendMsgToServer: function () {
            if (this.message.trim()) {
                this.webSocket.send(JSON.stringify({type: 'SPEAK', user: this.user, message: this.message.trim()}))
            }
            this.message = ''
        },
        clearMsg: function () {
            this.messages = []
        },
        changeNameToServer: function () {
            if (!this.user.name.trim()) {
                this.user.name = '匿名用户'
            }
            this.webSocket.send(JSON.stringify({type: 'CHANGE', user: this.user}))
            this.isChangingName = !this.isChangingName
        }
    }
})