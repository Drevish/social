import UserProfile from "./UserProfile";

class UserProfilesAutoFetchList extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            users: []
        }
    }

    fetchFriends() {
        fetch(this.props.userIdsUrl + this.props.userId)
            .then(response => response.json())
            .then(data => {
                Promise.all(data.map(id => fetch(this.props.userInfoUrl + id)
                    .then(response => response.json()))
                ).then(data => this.setState({
                    users: data
                }))
            });
    }

    componentDidMount() {
        this.fetchFriends();
    }

    render() {
        const users = this.state.users.map((info) => (
            <li key={info.id}>
                <UserProfile userInfo={info}/>
            </li>
        ));

        return (
            <ul className='profiles-list'>
                {users}
            </ul>
        );
    }
}

export default UserProfilesAutoFetchList;