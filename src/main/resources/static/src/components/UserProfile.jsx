function ProfileImage(props) {
    return !props.imageId ?
        <img src={'/image/profileImage/default.png'}/> :
        <img src={'/file/' + props.imageId}/>
}

function UserProfile(props) {
    const info = props.userInfo;
    return (
        <div className="container-fluid profile">
            <div className="row">
                <div className="col-2 profile-image-col">
                    <a className="profile-link" href={'/profile/id' + info.id}>
                        <ProfileImage imageId={info.imageId}/>
                    </a>
                </div>

                <div className="col-10 profile-info-col">
                    <a className="profile-link" href={'/profile/id' + info.id}>
                        <span className="name">{info.name} {info.surname}</span>
                    </a>
                </div>
            </div>
        </div>
    );
}

export default UserProfile;