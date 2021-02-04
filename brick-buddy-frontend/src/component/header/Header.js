import React, { Component } from 'react';
import { Link } from 'react-router-dom'
import Axios from 'axios';
import Buddy from './../../images/kid2.png';
import Popover from 'react-bootstrap/Popover';
import Overlay from 'react-bootstrap/Overlay';

class Header extends Component {
    state = {
        signin: {
            //These names have to match the Java class attribute names for Spring to call the appropriate setter.
            userID: '',
            userPassword: ''
        },
        error: ''
    }
    handleChange = (event) => {
        const value = event.target.value;
        const name = event.target.name;
        const tempSignin = { ...this.state.signin }
        tempSignin[name] = value;
        this.setState(
            {
                signin: tempSignin
            }
        )
    }
    handleSubmit = () => {
        this.setState(
            {
                error: ''
            }
        );
        // This will continue as an asyncronous call, does not hang the UI waiting for data from backend.
        Axios.post('http://localhost:4500/brickbuddy/login', this.state.signin)
            .then(response => {
                //capture customer info from the user which logged in.
                const email = response.data.email;
                localStorage.setItem('loggedInUser', email);
                //Navigate to home page. 
                this.props.history.push('/home');
            }).catch(error => {
                //Display error message.
                // console.log('handleSubmit ', error.response.status);
                if (error.response.status == 417) {
                    this.setState(
                        {
                            error: <p className="input-error back-blue my-auto mx-1 px-1">Username not found</p>
                        }
                    )
                };
                if (error.response.status == 401) {
                    this.setState(
                        {
                            error: <p className="input-error back-blue my-auto mx-1 px-1">Incorrect password</p>
                        }
                    )
                };
            });
    }
    signOut = () => {
        localStorage.removeItem("loggedInUser");
        this.props.history.push("/");
    }
    render() {
        let brickBuddy = (
            <Link className="navbar-brand text-gold back-blue px-2 py-auto" to="./landing">Brick Buddy<img src={Buddy} height="46" width="auto" /></Link>
        );
        let signInSignOut = (
            <React.Fragment>
                <form className="form-inline mt-0">
                    {this.state.error}
                    <input onChange={this.handleChange} className="form-control mx-1" name="userID" value={this.state.signin.userID} type="email" placeholder="email" aria-label="enter email" />
                    <input onChange={this.handleChange} className="form-control mx-1" name="userPassword" value={this.state.signin.userPassword} type="password" placeholder="password" aria-label="enter password" />
                    <button onClick={this.handleSubmit} className="btn btn-outline-warning btn-width back-blue text-gold mx-1" type="button">Sign In</button>
                </form>
                {/* <Overlay
            show={this.state.error}
            target= {useRef(null)}
            placement="bottom"
            // container={ref.current}
            containerPadding={20}
          >
            <Popover id="popover-contained">
              <Popover.Title as="h3">Login Error</Popover.Title>
              <Popover.Content>
                {this.state.error}
              </Popover.Content>
            </Popover>
          </Overlay> */}
            </React.Fragment>
        );
        let navLinks = (
            <li className="nav-item">
                <Link className="nav-link text-gold back-blue" aria-current="page" to="sign-up">Sign Up</Link>
            </li>
        );
        if (localStorage.getItem("loggedInUser")) {
            brickBuddy = (
                <Link className="navbar-brand text-gold back-blue px-2 py-auto" to="./home">Brick Buddy<img src={Buddy} height="46" width="auto" /></Link>
            );
            signInSignOut = (
                <form className="form-inline mt-2 mx-1 mt-md-0">
                    <button onClick={this.signOut} className="btn btn-outline-warning btn-width text-gold back-blue" type="button">Sign Out</button>
                </form>
            );
            navLinks = (
                <React.Fragment>
                    <li className="nav-item">
                        <Link className="nav-link text-gold back-blue mx-1" aria-current="page" to="my-page">
                            <i class="fas fa-heart" /><span className="pl-1">My Brick Buddy</span></Link>
                    </li>
                    <li className="nav-item">
                        <Link className="nav-link back-blue text-gold mx-1" aria-current="page" to="/home">
                            <i class="fas fa-table" /><span className="pl-1"> My Wanted List </span></Link>
                    </li>
                    <li className="nav-item">
                        <Link className="nav-link back-blue text-gold mx-1" aria-current="page" to="/vendor-search">
                            <i class="fas fa-shopping-cart" /><span className="pl-1">Check for Matching Vendors</span></Link>
                    </li>
                </React.Fragment>
            );
        }
        return (
            <div className="mb-5">

                <nav className="navbar navbar-expand-lg navbar-dark fixed-top bg-dark brick-bg float-center mb-2">
                    {brickBuddy}
                    <button className="navbar-toggler back-blue" type="button" data-bs-toggle="collapse" data-bs-target="#navbarCollapse" aria-controls="navbarCollapse" aria-expanded="false" aria-label="Toggle navigation">
                        <span className="navbar-toggler-icon"></span>
                    </button>
                    <div className="collapse navbar-collapse" id="navbarCollapse">
                        <ul className="navbar-nav mr-auto">
                            {navLinks}
                            <li className="nav-item">
                                <Link className="nav-link mx-1 text-gold back-blue" to="./about-us"><i class="far fa-smile" /><span className="pl-1">About Us</span></Link>
                            </li>

                        </ul>
                        {signInSignOut}
                    </div>
                </nav>
            </div>
        );
    }
}

export default Header;