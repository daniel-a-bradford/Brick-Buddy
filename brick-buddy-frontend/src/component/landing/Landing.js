import Axios from 'axios';
import React, { Component } from 'react';
import Sherlock from './../../images/sherlock.png';

class Landing extends Component {
    render() {
        return (
            <div>
    <header className="masthead head-foot-padding">
    <div className="container h-75">
      <div className="row h-75">
        <div className="col-lg-7 my-auto">
          <div className="header-content mx-auto">
            <h1 className="my-2">Brick Buddy is here to save you money. Simply tell Brick Buddy what you want, and he finds you the best deal.</h1>
          </div>
        </div>
        <div className="col-lg-5 centered my-auto">
          <a href="/sign-up" className="btn btn-outline btn-xl js-scroll-trigger back-blue text-gold mb-3">Set Up Your Wishlist For Free!</a>
          <div className="device-container">
            <div className="device-mockup macbook_2015 gold">
              <div className="device">
                <div className="screen">
                  <img src={Sherlock} className="img-fluid" alt=""/>
                </div>
               </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </header>
  <section className="download bg-primary text-center minbrick-bg" id="download">
    <div className="container">
      <div className="row">
        <div className="col-md-8 mx-auto">
        <h2 className="text-silver blue-shade">Let Brick Buddy find your ideal vendor.</h2>
        <p className="text-silver blue-shade">Buying multiple parts from one vendor cuts your shipping costs.</p>
          <div className="badges">
            <a className="badge-link" href="#"><img src="img/google-play-badge.svg" alt=""/></a>
            <a className="badge-link" href="#"><img src="img/app-store-badge.svg" alt=""/></a>
          </div>
        </div>
      </div>
    </div>
  </section>
            </div>
        );
    }
}

export default Landing;