import React from 'react';
import Founder from './../../images/DanProfile.png';
import Compbuild from './../../images/ComputerBuild.JPG';

const AboutUs = () => {
    return (
        <div className="about-body fallingbrick-bg head-foot-padding">
            <div className="container-fluid">
                <div className="row centered">
                    <div className="col-md-8">
                        <h1 className="headline blue-shade rounded">Danware provides amazing web applications.</h1>
                        <p className="blue-shade rounded">We provide solutions tailored to your needs, from our home to yours.</p>
                        <div className="row">
                            <div className="col-md-6">
                                <img src={Founder} width="300" height="300" alt="Photograph of Founder" />
                                <p className="blue-shade rounded">Danware founder Dan Bradford.</p>
                                <br />
                                <h5 className="headline blue-shade rounded">Since 2020, we’ve written thousands of lines of code in the pursuit of something better for our family.</h5>
                            </div>
                            <div className="col-md-6">
                                <br />
                                <br />
                                <br />
                                <h2 className="headline blue-shade rounded">Our Mission</h2>
                                <p className="blue-shade rounded">We believe that trust is built through excellence. We make software solutions not only to impress and get a job, but solutions which can be trusted to make your life better.</p>
                            </div>
                        </div>
                    </div>
                    <div className="col-md-4">
                        <img src={Compbuild} width="300" height="500" alt="Founder and son with homebuilt desktop computer" />
                        <h2 className="headline blue-shade rounded">We're here to help.</h2>
                        <p className="blue-shade rounded">When you join the Danware family, we care for you like on of our own.</p>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default AboutUs;