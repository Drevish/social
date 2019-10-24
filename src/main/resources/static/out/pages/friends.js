var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _possibleConstructorReturn(self, call) { if (!self) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return call && (typeof call === "object" || typeof call === "function") ? call : self; }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function, not " + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; }

function ProfileImage(props) {
    return !props.imageId ? React.createElement('img', { src: '/image/profileImage/default.png' }) : React.createElement('img', { src: '/file/' + props.imageId });
}

function UserProfile(props) {
    var info = props.userInfo;
    return React.createElement(
        'div',
        { className: 'container-fluid' },
        React.createElement(
            'div',
            { className: 'row' },
            React.createElement(
                'div',
                { className: 'col-1 profile-image-col' },
                React.createElement(
                    'a',
                    { className: 'profile-link', href: '/profile/id' + info.id },
                    React.createElement(ProfileImage, { imageId: info.imageId })
                )
            ),
            React.createElement(
                'div',
                { className: 'col-11 profile-info-col' },
                React.createElement(
                    'a',
                    { className: 'profile-link', href: '/profile/id' + info.id },
                    React.createElement(
                        'span',
                        { className: 'name' },
                        info.name,
                        ' ',
                        info.surname
                    )
                )
            )
        )
    );
}

var Friends = function (_React$Component) {
    _inherits(Friends, _React$Component);

    function Friends(props) {
        _classCallCheck(this, Friends);

        var _this = _possibleConstructorReturn(this, (Friends.__proto__ || Object.getPrototypeOf(Friends)).call(this, props));

        _this.state = {
            friends: [],
            incoming: [],
            upcoming: []
        };
        return _this;
    }

    _createClass(Friends, [{
        key: 'fetchFriends',
        value: function fetchFriends() {
            var _this2 = this;

            fetch('/api/friends/friend/' + userId).then(function (response) {
                return response.json();
            }).then(function (data) {
                Promise.all(data.map(function (id) {
                    return fetch('/api/userInfo/' + id).then(function (response) {
                        return response.json();
                    });
                })).then(function (data) {
                    return _this2.setState({
                        friends: data
                    });
                });
            });
        }
    }, {
        key: 'componentDidMount',
        value: function componentDidMount() {
            this.fetchFriends();
        }
    }, {
        key: 'render',
        value: function render() {
            var friends = this.state.friends.map(function (info) {
                return React.createElement(
                    'li',
                    { key: info.id },
                    React.createElement(UserProfile, { userInfo: info })
                );
            });

            return React.createElement(
                'ul',
                { className: 'profiles-list' },
                friends
            );
        }
    }]);

    return Friends;
}(React.Component);

//TODO: extract components

ReactDOM.render(React.createElement(Friends, null), document.getElementById('friends'));