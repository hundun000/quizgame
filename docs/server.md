# 快速上手

1. 创建比赛

POST /api/game/createEndlessMatch

body:
```json
{
    "teamNames": ["游客"],
    "questionPackageName": "questions"
}
```

使用预设的"游客"队伍（该队伍无英雄，无ban/pick），赛制为无限题数。

response:
```json
{
    "message": "SUCCESS",
    "status": 200,
    "payload": {
        "id": "0",
        "question": null,
        "currentTeamIndex": 0,
        "teamRuntimeInfos": [
            {
                "name": "游客",
                "roleName": null,
                "matchScore": 0,
                "skillRemainTimes": null,
                "buffs": [],
                "alive": true,
                "health": 1
            }
        ],
        "state": "WAIT_START",
        "answerResultEvent": null,
        "skillResultEvent": null,
        "switchQuestionEvent": null,
        "switchTeamEvent": null,
        "finishEvent": null,
        "startMatchEvent": null
    },
    "retcode": 0
}
```

创建了id = 0的比赛，后续请求api要用这个id。

2. 开始比赛

POST /api/game/start?sessionId=0

开始id = 0的比赛。

response:
```json
{
    "message": "SUCCESS",
    "status": 200,
    "payload": {
        "id": "0",
        "question": null,
        "currentTeamIndex": 0,
        "teamRuntimeInfos": [
            {
                "name": "游客",
                "roleName": null,
                "matchScore": 0,
                "skillRemainTimes": null,
                "buffs": [],
                "alive": true,
                "health": 1
            }
        ],
        "state": "WAIT_GENERATE_QUESTION",
        "answerResultEvent": null,
        "skillResultEvent": null,
        "switchQuestionEvent": null,
        "switchTeamEvent": null,
        "finishEvent": null,
        "startMatchEvent": {
            "type": "START_MATCH",
            "teamConstInfos": [
                {
                    "name": "游客",
                    "pickTags": [],
                    "banTags": [],
                    "roleName": null
                }
            ],
            "roleConstInfos": []
        }
    },
    "retcode": 0
}
```

有startMatchEvent。

3. 取题

POST /api/game/nextQustion?sessionId=0

id = 0的比赛中，为当前队伍取题。

response:
```json
{
    "message": "SUCCESS",
    "status": 200,
    "payload": {
        "id": "0",
        "question": {
            "id": "1dc2eca0",
            "stem": "TV动画《甲贺忍法帖》中，阳炎被谁斩获？",
            "options": [
                "药师寺天膳",
                "夜叉丸",
                "萤火",
                "胡夷"
            ],
            "answer": 0,
            "resource": {
                "type": "NONE",
                "data": null
            },
            "tags": [
                "甲贺忍法帖",
                "动画"
            ]
        },
        "currentTeamIndex": 0,
        "teamRuntimeInfos": [
            {
                "name": "游客",
                "roleName": null,
                "matchScore": 0,
                "skillRemainTimes": null,
                "buffs": [],
                "alive": true,
                "health": 1
            }
        ],
        "state": "WAIT_ANSWER",
        "answerResultEvent": null,
        "skillResultEvent": null,
        "switchQuestionEvent": {
            "type": "SWITCH_QUESTION",
            "time": 15
        },
        "switchTeamEvent": null,
        "finishEvent": null,
        "startMatchEvent": null
    },
    "retcode": 0
}
```

有switchQuestionEvent。

4. 答题

POST /api/game/answer?sessionId=0&answer=A

id = 0的比赛中，当前队伍答题，选择的是"A"选项。

response:
```json
{
    "message": "SUCCESS",
    "status": 200,
    "payload": {
        "id": "0",
        "question": null,
        "currentTeamIndex": 0,
        "teamRuntimeInfos": [
            {
                "name": "游客",
                "roleName": null,
                "matchScore": 1,
                "skillRemainTimes": null,
                "buffs": [],
                "alive": true,
                "health": 1
            }
        ],
        "state": "WAIT_GENERATE_QUESTION",
        "answerResultEvent": {
            "type": "ANSWER_RESULT",
            "result": "CORRECT",
            "addScore": 1,
            "addScoreTeamName": "游客"
        },
        "skillResultEvent": null,
        "switchQuestionEvent": null,
        "switchTeamEvent": null,
        "finishEvent": null,
        "startMatchEvent": null
    },
    "retcode": 0
}
```

有answerResultEvent。

5. 回到第3步进行下一题。

因为此时"state": "WAIT_GENERATE_QUESTION"。