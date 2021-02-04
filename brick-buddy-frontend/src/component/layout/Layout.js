import React, { Component } from 'react';
import { Route, withRouter } from 'react-router-dom';
import Header from '../header/Header';
import Signup from './../signup/Signup';
import AboutUs from './../aboutUs/AboutUs';
import Footer from './../footer/Footer';
import Home from './../home/Home';
import Landing from './../landing/Landing';
import MyPage from './../home/my-page/MyPage';
import VendorSearch from './../home/vendor-search/VendorSearch';

class Layout extends Component {
    render() {
        let routes = (
            <div>
                {/* if the user doesn't specify a url, go to the landing page */}
                <Route exact path="/" component={Landing} />
                <Route path="/landing" component={Landing} />
                <Route path="/sign-up" component={Signup} />
            </div>
        );
        if(localStorage.getItem("loggedInUser")) {
            routes = (
                <div>
                    <Route exact path="/" component={Home} />
                    <Route path="/landing" component={Landing} />
                    <Route path="/home" component={Home} />
                    <Route path="/my-page" component={MyPage} />
                    <Route path="/vendor-search" component={VendorSearch} />
                </div>
            );
        }
        return (
            <div>
                {/* prior to routing, Header does not have a way to navigate */}
                <Header {...this.props}/>
                <Route path="/about-us" component={AboutUs} />
                {routes}
                
                <Footer />
            </div>
        );
    }
}

export default withRouter(Layout);