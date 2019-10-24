function ProfileImage(props) {
    return !props.imageId ?
        <img src={'/image/profileImage/default.png'}/> :
        <img src={'/file/' + props.imageId}/>
}

function UserProfile(props) {
    const info = props.userInfo;
    return (
        <div className="container-fluid">
            <div className="row">
                <div className="col-1 profile-image-col">
                    <a className="profile-link" href={'/profile/id' + info.id}>
                        <ProfileImage imageId={info.imageId}/>
                    </a>
                </div>

                <div className="col-11 profile-info-col">
                    <a className="profile-link" href={'/profile/id' + info.id}>
                        <span className="name">{info.name} {info.surname}</span>
                    </a>
                </div>
            </div>
        </div>
    );
}

class Friends extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            friends: [],
            incoming: [],
            upcoming: []
        }
    }

    fetchFriends() {
        fetch('/api/friends/friend/' + userId)
            .then(response => response.json())
            .then(data => {
                Promise.all(data.map(id => fetch('/api/userInfo/' + id)
                    .then(response => response.json()))
                ).then(data => this.setState({
                    friends: data
                }))
            });
    }

    componentDidMount() {
        this.fetchFriends();
    }

    render() {
        const friends = this.state.friends.map((info) => (
            <li key={info.id}>
                <UserProfile userInfo={info}/>
            </li>
        ));

        return (
            <ul className='profiles-list'>
                {friends}
            </ul>
        );
    }
}

//TODO: extract components

ReactDOM.render(
    <Friends/>,
    document.getElementById('friends')
);
