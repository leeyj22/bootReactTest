const API_ENDPOINT = process.env.NEXT_PUBLIC_BASE_URL;

export const setPostApi = (postData) => {
    return {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(postData),
    };
};

export const api = {
    fetchGetTest: () => {
        return fetch(`${API_ENDPOINT}/api/getTest`, setPostApi()).then((res) =>
            res.json()
        );
    },
    fetchPostTest: (param) => {
        return fetch(`${API_ENDPOINT}/api/postTest`).then((res) => res.json());
    },
    fetchGetHoliday: (param) => {
        //접수 제한일 조회
        //param : 이전설치 T , 분해조립 A
        return fetch(
            `${API_ENDPOINT}/customerHelp/findErpHoliday?holidayType=${param}`
        ).then((res) => res.json());
    },
};

/*
GET요청
api.fetchGetHoliday(param).then((data) => {
    console.log('GET Response:', data);
}).catch((error) => {
    console.error('GET Error:', error);
});

*/

/* 
POST 요청

const postData = {
    // POST 요청에 필요한 데이터
    // 예: { key: value }
};

const requestOptions = {
    method: 'POST',
    headers: {
        'Content-Type': 'application/json',
    },
    body: JSON.stringify(postData),
};

fetch(`${API_ENDPOINT}/customerHelp/findErpHoliday`, requestOptions)
    .then((res) => res.json())
    .then((data) => {
        console.log('POST Response:', data);
    })
    .catch((error) => {
        console.error('POST Error:', error);
    });


*/
