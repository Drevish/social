var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

function _toConsumableArray(arr) { if (Array.isArray(arr)) { for (var i = 0, arr2 = Array(arr.length); i < arr.length; i++) { arr2[i] = arr[i]; } return arr2; } else { return Array.from(arr); } }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _possibleConstructorReturn(self, call) { if (!self) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return call && (typeof call === "object" || typeof call === "function") ? call : self; }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function, not " + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; }

var App = function (_React$Component) {
    _inherits(App, _React$Component);

    function App(props) {
        _classCallCheck(this, App);

        var _this = _possibleConstructorReturn(this, (App.__proto__ || Object.getPrototypeOf(App)).call(this, props));

        _this.state = {
            userInfos: [],
            friends: [],
            incoming: [],
            upcoming: []
        };
        return _this;
    }

    _createClass(App, [{
        key: 'fetchFriends',
        value: function fetchFriends() {
            var _this2 = this;

            fetch('/api/friends/upcoming/' + userId).then(function (response) {
                return response.json();
            }).then(function (data) {
                data.forEach(function (e) {
                    fetch('/api/userInfo/' + e).then(function (response) {
                        return response.json();
                    }).then(function (data) {
                        return _this2.setState(function (state) {
                            return {
                                friends: [].concat(_toConsumableArray(state.friends), [data])
                            };
                        });
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
            var friends = this.state.friends.map(function (userInfo) {
                return React.createElement(
                    'li',
                    null,
                    userInfo.name,
                    ' ',
                    userInfo.surname
                );
            });

            return React.createElement(
                'div',
                null,
                'Text ',
                React.createElement('br', null),
                React.createElement(
                    'h3',
                    null,
                    'Friends'
                ),
                React.createElement(
                    'ul',
                    null,
                    friends
                ),
                this.state.incoming,
                ' ',
                React.createElement('br', null),
                this.state.upcoming,
                ' ',
                React.createElement('br', null)
            );
        }
    }]);

    return App;
}(React.Component);

ReactDOM.render(React.createElement(App, null), document.getElementById('root'));