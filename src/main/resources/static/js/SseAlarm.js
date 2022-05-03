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
        const data = JSON.parse(event.data);
        alert(data.title);
        (async () => {
            // 브라우저 알림
            const showNotification = () => {
                const notification = new Notification('작성한 글에 댓글이 달렸습니다', {
                    body: data.title
                });

                setTimeout(() => {
                    notification.close();
                }, 10 * 1000);

                notification.addEventListener('click', () => {
                    window.open("/gallery/"+data.galleryName+"/"+data.boardIdx, '_blank');
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


