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

function addSSEEmitter()
{
    const eventSource = new EventSource("/commentAlarm/setEmitter");
    eventSource.addEventListener("sse", function (event) {
        const data = JSON.parse(event.data);
        (async () => {
            // 브라우저 알림
            const showNotification = () => {
                let alarmHead = "글 제목 : " +data.title + "에 댓글이 작성됐습니다";

                const notification = new Notification(alarmHead, {
                    body: "댓글 작성자 : " + data.user
                });

                setTimeout(() => {
                    notification.close();
                }, 10 * 1000);
                //TODO url 다듬기
                let url = "gallery/" + data.gallery +"/" + data.contentIdx;
                notification.addEventListener('click', () => {
                    //TODO 같은 url에서도 제대로 동작하도록 수정
                    window.open(url, '_blank');


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