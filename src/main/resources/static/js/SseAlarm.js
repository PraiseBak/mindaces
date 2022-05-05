function sendAlarm()
{
    var boardDto =
        {
            gallery : galleryName,
            contentIdx : boardIdx,
            title : title,
            user : userID
        }

    $.ajax({
        data : boardDto,
        method : 'GET',
        url : '/commentAlarm/send'
    });
}

function addSSEEmitter(user)
{
    alert(user);
    const eventSource = new EventSource("/commentAlarm/setEmitter/" + user);
    eventSource.addEventListener("sse", function (event) {
        const data = JSON.parse(event.data);
        (async () => {
            // 브라우저 알림
            const showNotification = () => {

                const notification = new Notification('코드 봐줘', {
                    body: data.content
                });

                setTimeout(() => {
                    notification.close();
                }, 10 * 1000);

                notification.addEventListener('click', () => {
                    window.open(data.url, '_blank');
                });
            }

            // 브라우저 알림 허용 권한
            let granted = false;

            if (Notification.permission === 'granted') {
                granted = true;
            } else if (Notification.permission !== 'denied') {
                let permission = await Notification.requestPermission();
                granted = permission === 'granted';
            }

            // 알림 보여주기
            if (granted) {
                showNotification();
            }
        })();
    })
}

/*

function sendAlarm()
{
    var boardDto =
        {
            gallery : galleryName,
            contentIdx : boardIdx,
            title : title,
            user : userID
        }

    $.ajax({
        data : boardDto,
        method : 'GET',
        url : '/commentAlarm/send'
    });
}

function addSSEEmitter(user)
{
    const eventSource = new EventSource(`/commentAlarm/` + user);
    alert("농1");

    eventSource.addEventListener("sse", function (event) {
        alert("농2");
        // const data = JSON.parse(event.data);
        (async () => {
            // 브라우저 알림
            const showNotification = () => {
                const notification = new Notification('작성한 글에 댓글이 달렸습니다', {
                    body: "농"
                });

                setTimeout(() => {
                    notification.close();
                }, 10 * 1000);

                notification.addEventListener('click', () => {
                    // window.open("/gallery/"+data.galleryName+"/"+data.boardIdx, '_blank');
                    window.open("/gallery/", '_blank');
                });
            }

            // 브라우저 알림 허용 권한
            let granted = false;

            if (Notification.permission === 'granted') {
                granted = true;
            } else if (Notification.permission !== 'denied') {
                let permission = await Notification.requestPermission();
                granted = permission === 'granted';
            }

            alert(granted);
            // 알림 보여주기
            if (granted) {
                showNotification();
            }
        })();
    })
}



 */