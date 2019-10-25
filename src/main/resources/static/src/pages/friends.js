import React from 'react';
import {render} from 'react-dom'

import UserProfilesAutoFetchList from "../components/UserProfilesAutoFetchList";

const userInfoUrl = '/api/userInfo/';
const friendsIdsUrl = '/api/friends/friend/';
const incomingIdsUrl = '/api/friends/incoming/';
const upcomingIdsUrl = '/api/friends/upcoming/';

render(
    <UserProfilesAutoFetchList userIdsUrl={friendsIdsUrl} userInfoUrl={userInfoUrl} userId={userId}/>,
    document.getElementById('friends')
);

render(
    <UserProfilesAutoFetchList userIdsUrl={incomingIdsUrl} userInfoUrl={userInfoUrl} userId={userId}/>,
    document.getElementById('incoming')
);

render(
    <UserProfilesAutoFetchList userIdsUrl={upcomingIdsUrl} userInfoUrl={userInfoUrl} userId={userId}/>,
    document.getElementById('upcoming')
);