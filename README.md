# 概述

一个答题游戏（一站到底）的后端程序。简单来说包含如下功能：

1. 题目可包括文本题干、图片、音频。

2. 每个题目拥有若干标签。每个队伍可以对标签进行ban、pick。

3. 可多个队伍参与同一局比赛，比拼得分。

4. 可自定义多种赛制。

    赛制具体涉及：

- 定义参赛队伍数量和轮换条件：单个队伍，多个队伍轮流，多个队伍直到当前队伍答错时换队……
- 定义比赛结束条件：每个队伍答满指定数量题目、某一个队伍连续答错n题时该队出局……

5. 每个队伍选择一个英雄角色。英雄角色可以使用不同技能。

    技能可分为：

- 瞬时技能：延长倒计时、排除错误选项、跳过本题……
- 持续增益：连续答对时获得额外得分……


6. 本后端程序通过http-api与客户端对接，可以有多种客户端实现方式：

- 【已上线】QQ-Bot作为客户端，通过QQ对话与用户交互。
- 【开发中】Unity程序作为客户端，通过Unity的UI与用户交互。
- 【开发中】本项目内置的JavaSwing应用作为客户端，通过UI控件输入输出文本与用户交互。

故本项目只提供api的开发手册。

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

# 开发说明

## 比赛前准备

介绍暂略，开发者可使用预设的队伍，跳过这一步。

## 比赛中

### 比赛状态MatchState

只会因客户端的请求而发生迁移，迁移时会产生若干MatchEvent子类。

比赛相关api每次正常返回的payload是一个MatchSituationDTO。MatchSituationDTO里会放入会放入本次请求引发迁移时产生的MatchEvent子类，和迁移后的MatchState，和其他数据。

其可构成一个Mealy型状态机：

[![](https://mermaid.ink/img/eyJjb2RlIjoiZmxvd2NoYXJ0IExSXG4gICAgY2xhc3NEZWYgc3RhdGVDb2xvciBmaWxsOiNmODgsc3Ryb2tlOiMzMzMsc3Ryb2tlLXdpZHRoOjNweFxuICAgIGNsYXNzRGVmIGV2ZW50Q29sb3IgZmlsbDojNEFGLHN0cm9rZTojMzMzLHN0cm9rZS13aWR0aDozcHhcblxuICAgIFN0YXJ0TWF0Y2hFdmVudChTdGFydE1hdGNoRXZlbnQpOjo6ZXZlbnRDb2xvclxuICAgIFN3aXRjaFF1ZXN0aW9uRXZlbnQoU3dpdGNoUXVlc3Rpb25FdmVudCk6OjpldmVudENvbG9yXG4gICAgU2tpbGxSZXN1bHRFdmVudChTa2lsbFJlc3VsdEV2ZW50KTo6OmV2ZW50Q29sb3JcbiAgICBBbnN3ZXJSZXN1bHRFdmVudChBbnN3ZXJSZXN1bHRFdmVudCk6OjpldmVudENvbG9yXG4gICAgU3dpdGNoVGVhbUV2ZW50KFN3aXRjaFRlYW1FdmVudCk6OjpldmVudENvbG9yXG4gICAgRmluaXNoRXZlbnQoRmluaXNoRXZlbnQpOjo6ZXZlbnRDb2xvclxuXG4gICAgV0FJVF9TVEFSVChbXCLmr5TotZvmnKrlvIDlp4s8YnI-TWF0Y2hTdGF0ZS5XQUlUX1NUQVJUXCJdKTo6OnN0YXRlQ29sb3JcbiAgICBXQUlUX0dFTkVSQVRFX1FVRVNUSU9OKFtcIuW-heWPlumimDxicj5NYXRjaFN0YXRlLldBSVRfR0VORVJBVEVfUVVFU1RJT05cIl0pOjo6c3RhdGVDb2xvclxuICAgIFdBSVRfQU5TV0VSKFtcIuW-heetlOmimDxicj5NYXRjaFN0YXRlLldBSVRfQU5TV0VSXCJdKTo6OnN0YXRlQ29sb3JcbiAgICBGSU5JU0hFRChbXCLmr5TotZvlt7Lnu5PmnZ88YnI-TWF0Y2hTdGF0ZS5GSU5JU0hFRFwiXSk6OjpzdGF0ZUNvbG9yXG5cbiAgICBmb3JrKFtcIuS4tOaXtuaAgVwiXSlcbiAgICBqb2luKFtcIuS4tOaXtuaAgVwiXSlcblxuICAgIFdBSVRfU1RBUlQgLS0-IHzlvIDlp4vlkb3ku6R8U3RhcnRNYXRjaEV2ZW50XG4gICAgU3RhcnRNYXRjaEV2ZW50IC0tPiBXQUlUX0dFTkVSQVRFX1FVRVNUSU9OXG5cbiAgICBXQUlUX0dFTkVSQVRFX1FVRVNUSU9OIC0tPiB85Y-W6aKY5ZG95LukfFN3aXRjaFF1ZXN0aW9uRXZlbnRcbiAgICBTd2l0Y2hRdWVzdGlvbkV2ZW50IC0tPiBXQUlUX0FOU1dFUlxuXG4gICAgV0FJVF9BTlNXRVIgLS0-IHzkvb_nlKjmioDog73lkb3ku6R8U2tpbGxSZXN1bHRFdmVudFxuICAgIFNraWxsUmVzdWx0RXZlbnQgLS0-IFdBSVRfQU5TV0VSXG4gICAgV0FJVF9BTlNXRVIgLS0-IHzmraPluLjnrZTpopgv6LaF5pe2L-i3s-i_h3xBbnN3ZXJSZXN1bHRFdmVudFxuICAgIEFuc3dlclJlc3VsdEV2ZW50IC0tPiBmb3JrXG5cbiAgICBmb3JrIC0tPiB85ruh6Laz5q-U6LWb57uT5p2f5p2h5Lu2fEZpbmlzaEV2ZW50XG4gICAgZm9yayAtLT4gfOa7oei2s-aNoumYn-adoeS7tnxTd2l0Y2hUZWFtRXZlbnRcbiAgICBmb3JrIC0tPiB85YW25LuWfGpvaW5cblxuICAgIFN3aXRjaFRlYW1FdmVudCAtLT4gam9pblxuICAgIGpvaW4gLS0-IFdBSVRfR0VORVJBVEVfUVVFU1RJT05cblxuICAgIEZpbmlzaEV2ZW50IC0tPiBGSU5JU0hFRFxuXG4iLCJtZXJtYWlkIjp7InRoZW1lIjoiZGVmYXVsdCJ9LCJ1cGRhdGVFZGl0b3IiOmZhbHNlLCJhdXRvU3luYyI6dHJ1ZSwidXBkYXRlRGlhZ3JhbSI6ZmFsc2V9)](https://mermaid-js.github.io/mermaid-live-editor/edit/##eyJjb2RlIjoiZmxvd2NoYXJ0IExSXG4gICAgY2xhc3NEZWYgc3RhdGVDb2xvciBmaWxsOiNmODgsc3Ryb2tlOiMzMzMsc3Ryb2tlLXdpZHRoOjNweFxuICAgIGNsYXNzRGVmIGV2ZW50Q29sb3IgZmlsbDojNEFGLHN0cm9rZTojMzMzLHN0cm9rZS13aWR0aDozcHhcblxuICAgIFN0YXJ0TWF0Y2hFdmVudChTdGFydE1hdGNoRXZlbnQpOjo6ZXZlbnRDb2xvclxuICAgIFN3aXRjaFF1ZXN0aW9uRXZlbnQoU3dpdGNoUXVlc3Rpb25FdmVudCk6OjpldmVudENvbG9yXG4gICAgU2tpbGxSZXN1bHRFdmVudChTa2lsbFJlc3VsdEV2ZW50KTo6OmV2ZW50Q29sb3JcbiAgICBBbnN3ZXJSZXN1bHRFdmVudChBbnN3ZXJSZXN1bHRFdmVudCk6OjpldmVudENvbG9yXG4gICAgU3dpdGNoVGVhbUV2ZW50KFN3aXRjaFRlYW1FdmVudCk6OjpldmVudENvbG9yXG4gICAgRmluaXNoRXZlbnQoRmluaXNoRXZlbnQpOjo6ZXZlbnRDb2xvclxuXG4gICAgV0FJVF9TVEFSVChbXCLmr5TotZvmnKrlvIDlp4s8YnI-TWF0Y2hTdGF0ZS5XQUlUX1NUQVJUXCJdKTo6OnN0YXRlQ29sb3JcbiAgICBXQUlUX0dFTkVSQVRFX1FVRVNUSU9OKFtcIuW-heWPlumimDxicj5NYXRjaFN0YXRlLldBSVRfR0VORVJBVEVfUVVFU1RJT05cIl0pOjo6c3RhdGVDb2xvclxuICAgIFdBSVRfQU5TV0VSKFtcIuW-heetlOmimDxicj5NYXRjaFN0YXRlLldBSVRfQU5TV0VSXCJdKTo6OnN0YXRlQ29sb3JcbiAgICBGSU5JU0hFRChbXCLmr5TotZvlt7Lnu5PmnZ88YnI-TWF0Y2hTdGF0ZS5GSU5JU0hFRFwiXSk6OjpzdGF0ZUNvbG9yXG5cbiAgICBmb3JrKFtcIuS4tOaXtuaAgVwiXSlcbiAgICBqb2luW1wi5ZCI5bm26IqC54K5XCJdXG5cbiAgICBXQUlUX1NUQVJUIC0tPiB85byA5aeL5ZG95LukfFN0YXJ0TWF0Y2hFdmVudFxuICAgIFN0YXJ0TWF0Y2hFdmVudCAtLT4gV0FJVF9HRU5FUkFURV9RVUVTVElPTlxuXG4gICAgV0FJVF9HRU5FUkFURV9RVUVTVElPTiAtLT4gfOWPlumimOWRveS7pHxTd2l0Y2hRdWVzdGlvbkV2ZW50XG4gICAgU3dpdGNoUXVlc3Rpb25FdmVudCAtLT4gV0FJVF9BTlNXRVJcblxuICAgIFdBSVRfQU5TV0VSIC0tPiB85L2_55So5oqA6IO95ZG95LukfFNraWxsUmVzdWx0RXZlbnRcbiAgICBTa2lsbFJlc3VsdEV2ZW50IC0tPiBXQUlUX0FOU1dFUlxuICAgIFdBSVRfQU5TV0VSIC0tPiB85q2j5bi4562U6aKYL-i2heaXti_ot7Pov4d8QW5zd2VyUmVzdWx0RXZlbnRcbiAgICBBbnN3ZXJSZXN1bHRFdmVudCAtLT4gZm9ya1xuXG4gICAgZm9yayAtLT4gfOa7oei2s-avlOi1m-e7k-adn-adoeS7tnxGaW5pc2hFdmVudFxuICAgIGZvcmsgLS0-IHzmu6HotrPmjaLpmJ_mnaHku7Z8U3dpdGNoVGVhbUV2ZW50XG4gICAgZm9yayAtLT4gfOWFtuS7lnxqb2luXG5cbiAgICBTd2l0Y2hUZWFtRXZlbnQgLS0-IGpvaW5cbiAgICBqb2luIC0tPiBXQUlUX0dFTkVSQVRFX1FVRVNUSU9OXG5cbiAgICBGaW5pc2hFdmVudCAtLT4gRklOSVNIRURcblxuIiwibWVybWFpZCI6IntcbiAgXCJ0aGVtZVwiOiBcImRlZmF1bHRcIlxufSIsInVwZGF0ZUVkaXRvciI6ZmFsc2UsImF1dG9TeW5jIjp0cnVlLCJ1cGRhdGVEaWFncmFtIjpmYWxzZX0)



