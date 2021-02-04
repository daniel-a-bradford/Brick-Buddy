import React, { Component } from 'react';
import Axios from 'axios';
import './bounce.css';
import Dropdown from 'react-bootstrap/Dropdown';
import Sherlock from './../../../images/sherlock.png';
import Explorer from './../../../images/explorer.png';
import Pilot from './../../../images/pilot.png';
import Cheerleader from './../../../images/cheerleader.png';
import Diver from './../../../images/diver.png';
import Emmett from './../../../images/emmett.png';
import Ladder from './../../../images/ladder.png';
import Arch from './../../../images/arch.png';
import Bridge from './../../../images/bridge.png';
import CompGuy from './../../../images/comp-guy.png';
import Carpenter from './../../../images/carpenter.png';
import Turkey from './../../../images/turkey.png';
import Surgeon from './../../../images/surgeon.png';

class VendorSearch extends Component {
    state = {
        customer: {},
        wantList: [],
        vendorList: [],
        tempAddCost: '',
        searched: '',
        searching: '',
        selectedVendor: {
            vendorID: '',
            name: '',
            state: '',
            country: '',
            link: '',
            minBuy: '',
            terms: '',
            matches: '',
            partsCost: '',
            shipCost: '',
            addedCost: '',
            matchingParts: []
        },
        selectedPart: {
            partID: '',
            type: '',
            number: '',
            name: '',
            colorNum: '',
            cond: '',
            quantity: '',
            condDescription: '',
            price: '',
            cartAddURL: ''
        }
    }
    componentDidMount() {
        const email = localStorage.getItem("loggedInUser");
        const params = { email };
        Axios.get('http://localhost:4500/brickbuddy/findCustomerByEmail', { params })
            .then(response => {
                this.setState(
                    {
                        customer: response.data
                    }
                )
            }).catch(error => {
                console.log('findCustomerByEmail ', error.response.status);
            })
        Axios.get('http://localhost:4500/brickbuddy/getCustomerWantList', { params })
            .then(response => {
                this.setState(
                    {
                        wantList: response.data
                    }
                )
            }).catch(error => {
                console.log('getCustomerWantList ', error.response.status);
            })
        Axios.get('http://localhost:4500/brickbuddy/getMatchingVendorList', { params })
            .then(response => {
                this.setState(
                    {
                        vendorList: response.data
                    }
                )
            }).catch(error => {
                console.log('getMatchingVendorList ', error.response.status);
            })
    }
    handleFindVendors = () => {
        const searchCustomer = this.state.customer;
        this.setState(
            {
                vendorList: [],
                searched: '',
                searching: 'true'
            }
        )
        Axios.post('http://localhost:4500/brickbuddy/findMatchingVendors', searchCustomer)
            .then(response => {
                this.setState(
                    {
                        vendorList: response.data,
                        searched: 'true',
                        searching: 'false'
                    }
                )
                this.props.history.push('/vendor-search');
            }).catch(error => {
                console.log('getCustomerWantList ', error.response.status);
            })
    }
    handleClearVendorList = () => {
        const email = this.state.customer.email;
        const params = { email };
        this.setState(
            {
                vendorList: [],
                searched: ''
            }
        )
        Axios.get('http://localhost:4500/brickbuddy/clearMatchingVendorList', { params })
            .then(response => {
                console.log("clearMatchingVendorList - vendor list cleared for ", email)
            }).catch(error => {
                console.log('clearMatchingVendorList ', error.response.status);
            })
    }
    handleChange = (event) => {
        const value = event.target.value;
        this.setState(
            {
                tempAddCost: value
            }
        )
    }
    handleCalc = (vendor) => {
        let tempVendor = vendor;
        tempVendor.addedCost = this.state.tempAddCost;
        this.setState(
            {
                selectedVendor: tempVendor
            }
        )
        Axios.post('http://localhost:4500/brickbuddy/updateVendorAddedCost', tempVendor)
            .then(response => {
                this.setState(
                    {
                        vendorList: response.data,
                        tempAddCost: '',
                        selectedVendor: {
                            vendorID: '',
                            name: '',
                            state: '',
                            country: '',
                            link: '',
                            minBuy: '',
                            terms: '',
                            matches: '',
                            partsCost: '',
                            shipCost: '',
                            addedCost: '',
                            matchingParts: []
                        }
                    }
                )
                // this.props.history.push('/vendor-search');
            }).catch(error => {
                console.log('updateVendorAddedCost', error.response.status);
            })
    }
    handleSelect = (vendor) => {
        const tempVendor = vendor;
        this.setState(
            {
                selectedVendor: tempVendor
            }
        )
    }
    formatDecimal(num, trail) {
        var numString = num.toString();

        var wholeNum, decimals, decimalIndex = numString.indexOf(".");

        // If there are no decimal places, then it is a whole number
        if (decimalIndex == -1) {
            wholeNum = numString.length;
            decimals = 0;
            numString += ".";
        } else {
            wholeNum = decimalIndex;
            decimals = numString.substr(decimalIndex + 1).length;
            // Gets rid of all decimal numbers after the value of trail
            decimals = decimals.toString().substring(0, trail);
        }
        // Adds padding as necessary
        if (decimals < trail) {
            for (let i = decimals; i < trail; i++) { numString += "0"; }
        }
        return numString;
    }
    render() {
        let searchButton = (
            <div className="container centered">
                <h3 className="back-green text-gold">Give Brick Buddy a Challenge!</h3>
                <p className="back-green text-gold">Add 2 or more items to your want list to get started. Ordering one at a time will not save you money. </p>
            </div>
        );

        let resultsTable = (
            <div className="container head-foot-padding">
                <div className="row">
                </div>
            </div>
        );

        if (this.state.searching) {
            searchButton = (
                <div className="container centered"></div>
            );
        }

        if (this.state.wantList.length > 1 & !this.state.searching) {
            searchButton = (
                <button onClick={this.handleFindVendors} className="btn btn-primary back-blue text-gold text-center"
                    type="button">Search for Vendors (This may take a several seconds)</button>
            );
            resultsTable = (
                <div className="container centered">
                    <h3 className="back-blue text-gold">Brick Buddy Will Show Your Results Below</h3>
                    <p className="back-blue text-gold">Please note: Depending on how many vendors are offering your want list items, your search may take several seconds.</p>
                </div>
            );
        }

        if (this.state.wantList.length > 1 && this.state.searching) {
            resultsTable = (
                <div className="container head-foot-padding">
                    <div className="row">
                        <div className="x square">
                            <div className="y square">
                                <img src={Ladder} width="150px" height="auto" alt="" />
                            </div>
                        </div>
                    </div>
                    <div className="x2 square">
                        <div className="y2 square">
                            <img src={Emmett} width="150px" height="auto" alt="" />
                        </div>
                    </div>
                    <div className="x3 square">
                        <div className="y3 square">
                            <img src={Turkey} width="150px" height="auto" alt="" />
                        </div>
                    </div>
                    <div className="x4 square">
                        <div className="y4 square">
                            <img src={Surgeon} width="150px" height="auto" alt="" />
                        </div>
                    </div>
                    <div className="x5 square">
                        <div className="y5 square">
                            <img src={Bridge} width="150px" height="auto" alt="" />
                        </div>
                    </div>
                    <div className="x6 square">
                        <div className="y6 square">
                            <img src={Arch} width="150px" height="auto" alt="" />
                        </div>
                    </div>
                    <div className="x7 square">
                        <div className="y7 square">
                            <img src={Carpenter} width="150px" height="auto" alt="" />
                        </div>
                    </div>
                    <div className="x8 square">
                        <div className="y8 square">
                            <img src={CompGuy} width="150px" height="auto" alt="" />
                        </div>

        //         </div>
                </div>
            );
        }

        if ((this.state.vendorList.length == 0) && this.state.searched) {
            resultsTable = (
                <div className="container centered">
                    <h1 className="h2 back-blue text-gold">Sorry! Brick Buddy Did Not Find Any Matching Vendors.</h1>
                    <h4 className="h2 back-blue text-gold">Often vendors have limited stock. Here are some tips to get results:</h4>
                    <h5 className="h2 back-blue text-gold">1. Reduce the number of a certain item you want.</h5>
                    <h5 className="h2 back-blue text-gold">2. If you are okay with a used part, change condition to "Used," or "Any".</h5>
                    <h5 className="h2 back-blue text-gold">3. Try a more common color or change the item to a similar or related part.</h5>
                </div>
            );
        }

        if (this.state.vendorList.length > 0) { /* this.state.vendorList.length > 0 */
            searchButton = (
                <button onClick={this.handleClearVendorList} className="btn btn-secondary back-red text-black text-center"
                    type="button">Clear Previous Results</button>
            );
            resultsTable = (
                <div className="container">
                    <h1 className="h2 back-blue text-gold">Brick Buddy Found {this.state.vendorList.length} Vendors - Select One To Purchase</h1>
                    <div className="table-responsive">
                        <table className="table table-sm table-striped table-hover centered">
                            <thead>
                                <tr className="d-flex back-green text-gold">
                                    <th className="col-2">Name and<div>Location</div></th>
                                    <th className="col-4">Matching Items<div>(click each for details)</div></th>
                                    <th className="col-1">Item Cost<div>Min Buy</div></th>
                                    <th className="col-2">Est. Shipping</th>
                                    <th className="col-2">Vendor Added Cost<div>(from Terms)</div></th>
                                    <th className="col-1">Total Cost</th>
                                </tr>
                            </thead>
                            <tbody>
                                {this.state.vendorList.map((Vendor, index) =>
                                    <tr className="d-flex back-blue back-green text-gold">
                                        <td className="col-2 pt-3"><a href="#vendorDetails" onClick={() => this.handleSelect(Vendor)} className="btn btn-primary back-blue text-gold regular-case" data-toggle="modal">{Vendor.name}<div>
                                        </div></a></td>
                                        <td className="col-4 pt-3">
                                            <div className="row">{Vendor.matchingParts.map((StockedPart) =>
                                                <div className="col">
                                                    <Dropdown drop="up" flip="true">
                                                        <Dropdown.Toggle className="back-blue text-gold" id="basic-nav-dropdown">{<img src={StockedPart.imageURL} width="50px" height="50px" />}</Dropdown.Toggle>
                                                        <Dropdown.Menu className="back-blue text-gold">
                                                            <Dropdown.Item>{<img src={StockedPart.imageURL} width="100px" height="100px" />}</Dropdown.Item>
                                                            <Dropdown.Item className="text-gold">{StockedPart.cond} {StockedPart.name} for ${this.formatDecimal(StockedPart.price)}</Dropdown.Item>
                                                            <Dropdown.Item className="text-gold">Additional Info: {StockedPart.condDescription}</Dropdown.Item>
                                                        </Dropdown.Menu>
                                                    </Dropdown>
                                                </div>
                                            )}
                                            </div>
                                        </td>
                                        <td className="col-1">${this.formatDecimal(+Vendor.partsCost.toFixed(2), 2)}
                                            <div className={(Vendor.partsCost < Vendor.minBuy) ? "text-black back-red" : ""}>${this.formatDecimal(+Vendor.minBuy.toFixed(2), 2)}</div></td>
                                        <td className="col-2">${this.formatDecimal(+Vendor.shipCost.toFixed(2), 2)} from <div>{Vendor.location}</div> <div>to {this.state.customer.location}</div></td>
                                        <td className="col-2">
                                            <a href={Vendor.terms} className="btn btn-primary back-blue text-gold m-1" type="button" target="_blank" rel="noreferrer noopener">Terms</a>
                                            <input type="text" onChange={this.handleChange} id="pnum" name="addedCost"
                                                className="form-control" placeholder="Enter cost then calc" value={this.state.addedCost} placeholder={Vendor.addedCost}
                                                aria-label="Click TERMS to see vendor terms and manually add any additional costs." />
                                        </td>

                                        <td className="col-1">${(Vendor.partsCost < Vendor.minBuy) ? this.formatDecimal((+Vendor.minBuy + +Vendor.shipCost + +Vendor.addedCost).toFixed(2), 2)
                                            : this.formatDecimal((+Vendor.partsCost + +Vendor.shipCost + +Vendor.addedCost).toFixed(2), 2)}<div>
                                                <button onClick={() => this.handleCalc(Vendor)} className="btn btn-primary back-blue text-gold mt-3 m-1">Calc</button></div>
                                        </td>

                                    </tr>
                                )}
                            </tbody>
                        </table>
                    </div>
                </div>
            );
        }
        return (
            <div className="fullscreen city-bg">
                <div className="container-fluid fullscreen head-foot-padding">
                    <div className="row">
                        <nav className="col-md-2 d-none d-sm-block brick-bg sidebar">
                            <div className="sidebar-sticky">
                                <img className="pl-2" src={Sherlock} height="450px" width="auto" />
                                <img src={Explorer} height="350px" width="auto" />
                                <img src={Cheerleader} height="350px" width="auto" />
                                <img src={Pilot} height="350px" width="auto" />
                                <img src={Diver} height="350px" width="auto" />
                            </div>
                        </nav>

                        <main role="main" className="col-md-9 ml-sm-auto col-lg-10 px-4">
                            <h1 className="h2 back-blue text-gold mt-3">{this.state.customer.firstName} {this.state.customer.lastName}'s Want List</h1>
                            <div className="row pb-2">
                                {this.state.wantList.map((wantedPart, index) =>
                                    <div className="col-md-3">
                                        <div className="card shadow-sm back-green text-gold">
                                            <div className="card-body centered">
                                                <img src={wantedPart.imageURL} width="100px" height="100px" />
                                                <p className="card-text">{wantedPart.name}</p>
                                                <p className="card-text">{wantedPart.cond} Condition</p>
                                                <p className="card-text">{wantedPart.quantity} Desired</p>
                                            </div>
                                        </div>
                                    </div>
                                )}
                            </div>
                            <div className="centered">
                                {searchButton}
                            </div>
                            <div className="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                            </div>
                            {resultsTable}
                        </main>
                    </div>
                    {/* Beginning of vendorDetails Modal */}
                    <div id="vendorDetails" className="modal fade">
                        <div className="modal-dialog modal-lg text-gold">
                            <div className="modal-content back-green">
                                <div className="modal-header brick-bg">
                                    <h3 className="modal-title back-blue">{this.state.selectedVendor.name} in {this.state.selectedVendor.location}</h3>
                                    <button type="button" className="close text-gold" data-dismiss="modal" aria-hidden="true">&times;</button>
                                </div>
                                <div className="modal-body">
                                    <div className="centered">
                                        <h4 className="back-blue">You're almost there! Please review the details below:</h4>
                                        <br />
                                        <h5>{this.state.selectedVendor.name} has {this.state.selectedVendor.matches} of {this.state.wantList.length} items from your want list.</h5>
                                        <h5 className="text-black back-red">{(this.state.selectedVendor.partsCost < this.state.selectedVendor.minBuy) ? ("They require you to purchase at least $" +
                                            (this.formatDecimal(+this.state.selectedVendor.minBuy - +this.state.selectedVendor.partsCost), 2).toString() + " worth of items") : ""}</h5>
                                        <br />
                                    </div>
                                    <div className="row">
                                        <div className="col-md-2"></div>
                                        <div className="col-md-4 pl-1">
                                            <h5>Cost of items: </h5>
                                            <h5>Estimated shipping:</h5>
                                            <h5>Vendor added cost:</h5>
                                            <small className="ml-1">_______________________________________________</small>
                                            <h5>Estimated total: </h5>
                                        </div>
                                        <div className="col-md-1 mr-2">
                                            <h5>${this.formatDecimal(+this.state.selectedVendor.partsCost, 2)}</h5>
                                            <h5>${this.formatDecimal(+this.state.selectedVendor.shipCost, 2)}</h5>
                                            <h5>${this.formatDecimal(+this.state.selectedVendor.addedCost, 2)}</h5>
                                            <small className="ml-1">_______________</small>
                                            <h5>${(this.state.selectedVendor.partsCost < this.state.selectedVendor.minBuy)
                                                ? this.formatDecimal((+this.state.selectedVendor.minBuy + +this.state.selectedVendor.shipCost + +this.state.selectedVendor.addedCost).toFixed(2), 2)
                                                : this.formatDecimal((+this.state.selectedVendor.partsCost + +this.state.selectedVendor.shipCost + +this.state.selectedVendor.addedCost).toFixed(2), 2)}</h5>
                                        </div>
                                        <div className="col-md-2 mx-2">
                                            <a href={this.state.selectedVendor.terms} className="btn btn-block back-blue text-gold m-1" type="button" target="_blank" rel="noreferrer noopener">Terms</a>
                                            <input type="text" onChange={this.handleChange} id="pnum" name="addedCost"
                                                className="form-control ml-1" placeholder="Enter cost"
                                                aria-label="Click TERMS to see vendor terms and manually add any additional costs." />
                                            <button onClick={() => this.handleCalc(this.state.selectedVendor)} className="btn btn-block back-blue text-gold m-1">Calc</button>
                                        </div>
                                    </div>
                                    <div className="centered">
                                        <small>Sometimes vendors add unforeseen costs. Please click "TERMS" enter any additional costs.</small>
                                        <div><small>Then click "Calc" to update the total estimated cost.</small></div>
                                        <br />
                                        <h4>When you are satisfied, click the links in turn to add them to your cart.</h4>
                                        {this.state.selectedVendor.matchingParts.map((stockedParts, index) =>
                                            <div><img className="mr-3" src={stockedParts.imageURL} width="50px" height="50px" />
                                                <a className="text-gold" href={stockedParts.cartAddURL} target="_blank" rel="noreferrer noopener">{stockedParts.cartAddURL}</a>
                                            </div>
                                        )}
                                    </div>
                                </div>
                                <div className="modal-footer brick-bg">
                                    <button type="button" className="btn btn-default back-blue text-gold" data-dismiss="modal">Close</button>
                                </div>
                            </div>
                        </div>
                    </div>
                    {/* End of vendorDetails Modal */}
                </div>
            </div>

        );
    }
}

export default VendorSearch;