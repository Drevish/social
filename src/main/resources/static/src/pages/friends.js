class App extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            userInfos: [],
            friends: [],
            incoming: [],
            upcoming: []
        }
    }

    fetchFriends() {
        fetch('/api/friends/upcoming/' + userId)
            .then(response => response.json())
            .then(data => {
                data.forEach(e => {
                    fetch('/api/userInfo/' + e)
                        .then(response => response.json())
                        .then(data =>
                            this.setState(state => ({
                                friends: [...state.friends, data]
                            }))
                        );
                });
            });
    }

    componentDidMount() {
        this.fetchFriends();
    }

    render() {
        const friends = this.state.friends.map((userInfo) => (
            <li>
                {userInfo.name} {userInfo.surname}
            </li>
        ));

        return (
            <div>
                Text <br/>
                <h3>Friends</h3>
                <ul>
                    {friends}
                </ul>
                {this.state.incoming} <br/>
                {this.state.upcoming} <br/>
            </div>
        );
    }
}

ReactDOM.render(
    <App/>,
    document.getElementById('root')
);